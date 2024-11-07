package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.application.facade.BookFacade;
import io.hhplus.concertbook.application.facade.PayFacade;
import io.hhplus.concertbook.common.exception.*;
import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.dto.SeatDto;
import io.hhplus.concertbook.domain.service.ConcertService;
import io.hhplus.concertbook.domain.service.PaymentService;
import io.hhplus.concertbook.presentation.HttpDto.request.ConcertBookReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.PayReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/concert")
@Tag(name = "Concert Info", description = "콘서트 정보 조회")
public class ConcertBookController {

    @Autowired
    ConcertService concertService;

    @Autowired
    BookFacade bookFacade;

    @Autowired
    PayFacade payFacade;

    @GetMapping("/date")
    @Operation(summary = "공연스케줄조회", description = "공연스케줄조회")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> getAvailableDates(
            @RequestBody
            @Parameter(required = true, description = "콘서트날짜입력")
            @RequestParam(value ="concertd", required = true) String concertD )  {

//        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(concertD, formatter);
//        }catch (DateTimeException e) {
//            throw new DateParameterException("날짜 형식 부정확");
//        }

        List<ConcertScheduleDto> concertScheduleDtos = concertService.getAvailSchedule(concertD);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(concertScheduleDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/seat")
    @Operation(summary = "공연좌석조회", description = "좌석 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> getAvailableSeats(

            @Parameter(required = true, description = "콘서트정보입력")
            @RequestParam(value = "itemid") Long itemId)  {


        List<SeatDto> seatDtos = concertService.getSeats(itemId);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(seatDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/book")
    @Operation(summary = "콘서트", description = "콘서트 예약")
    public ResponseEntity<CommonResponse<Object>> requestSeatReservation(
            @RequestBody ConcertBookReqDto concertBookReqDto,
            @SessionAttribute("user") String sessionUser
            ) throws Exception {

//        if(concertReservReqDto.getConcertScheduleId() == null) {
//            throw new NoIdException("콘서트 스케줄 정보가 없습니다.");
//        }
        if(concertBookReqDto.getSeatId() == null) {
            throw new CustomException(ErrorCode.SEAT_ERROR);
        }

        log.info("sessionUser {} token {}",sessionUser,concertBookReqDto.getToken());
        if(concertBookReqDto.getToken() == null) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        if(!concertBookReqDto.getToken().startsWith(sessionUser)){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        long bookId = bookFacade.book(concertBookReqDto.getToken(), concertBookReqDto.getSeatId());

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(bookId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @PostMapping("/payments")
    @Operation(summary = "결제", description = "결제")
    public ResponseEntity<CommonResponse<Object>> makePayment(
        @RequestBody PayReqDto payReqDto,
        @SessionAttribute("user") String sessionUser
            ) throws Exception {

        if(payReqDto.getBookId() == null) {
            throw new CustomException(ErrorCode.BOOK_ERROR);
        }

        if(payReqDto.getToken() == null) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        if(!payReqDto.getToken().startsWith(sessionUser)) { //세션의 유저명이 토큰이랑 불일치
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        boolean result = payFacade.pay(payReqDto.getToken(), payReqDto.getBookId());

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //TEST 용 cacheEvict
    @GetMapping("/evictConcertSchedule")
    @CacheEvict(value = "concertSchedule", key = "#scheduleDate")
    public void evictCache(@RequestParam(value ="concertd", required = true) String scheduleDate) {
        // 캐시를 비우는 로직
    }
}