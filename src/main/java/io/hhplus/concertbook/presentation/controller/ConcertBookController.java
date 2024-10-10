package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.common.exception.DateParameterException;
import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.presentation.HttpDto.request.ConcertDateReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.ConcertReservReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.ConcertSeatReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.PayReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/concerts")
@Tag(name = "Concert Info", description = "콘서트 정보 조회")
public class ConcertBookController {

    @PostMapping("/date")
    @Operation(summary = "공연스케줄조회", description = "공연스케줄조회")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> getAvailableDates(
            @RequestBody
            @Parameter(required = true, description = "콘서트날짜입력")
            ConcertDateReqDto concertDateReqDto) throws NoTokenException {

        if(concertDateReqDto.getToken() == null) {
            throw new NoTokenException("토큰이 없습니다.");
        }


        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(concertDateReqDto.getDateyymmdd(), formatter);
        }catch (DateTimeException e) {
            throw new DateParameterException("날짜 형식 부정확");
        }

        String timemmdd = "030000";
        Long concertId = 100L;
        int availSeats = 50;
        String concertName = "뉴진스콘서트";

        ConcertDateResDto concertDateResDto = new ConcertDateResDto(
                concertDateReqDto.getDateyymmdd(),timemmdd,concertId,concertName,availSeats);
        List<ConcertDateResDto> concertDateResDtoList = new ArrayList<>();
        concertDateResDtoList.add(concertDateResDto);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(concertDateResDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/seat")
    @Operation(summary = "공연좌석조회", description = "좌석 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> getAvailableSeats(
            @RequestBody
            @Parameter(required = true, description = "콘서트정보입력")
            ConcertSeatReqDto concertSeatReqDto) throws NoTokenException, NoIdException {

        if(concertSeatReqDto.getToken() == null) {
            throw new NoTokenException("토큰이 없습니다.");
        }

        if(concertSeatReqDto.getConcertScheduleId() == null) {
            throw new NoIdException("Id가 없습니다.");
        }
        

        int seatNo = 50;
        Long seatId = 50L;

        ConcertSeatResDto concertSeatResDto =
                new ConcertSeatResDto(seatNo,seatId,concertSeatReqDto.getConcertScheduleId());

        List<ConcertSeatResDto> concertSeatResDtoList = new ArrayList<>();
        concertSeatResDtoList.add(concertSeatResDto);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(concertSeatResDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/reservations")
    @Operation(summary = "콘서트", description = "콘서트 예약")
    public ResponseEntity<CommonResponse<Object>> requestSeatReservation(
            @RequestBody ConcertReservReqDto concertReservReqDto
            ) throws NoIdException {

        if(concertReservReqDto.getConcertScheduleId() == null) {
            throw new NoIdException("콘서트 스케줄 정보가 없습니다.");
        }
        if(concertReservReqDto.getSeatId() == null) {
            throw new NoIdException("콘서트 좌석 정보가 없습니다.");
        }
        Long reservId = 20L;
        String concertName = "뉴진스콘서트";
        ConcertReservResDto concertReservResDto = new ConcertReservResDto(true,reservId,concertName);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(concertReservResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @PostMapping("/payments")
    @Operation(summary = "결제", description = "결제")
    public ResponseEntity<CommonResponse<Object>> makePayment(
        @RequestBody PayReqDto payReqDto
            ) throws NoIdException {
        if(payReqDto.getReservId() == null) {
            throw new NoIdException("id가 없습니다.");
        }

        PayResDto payResDto = new PayResDto(payReqDto.getReservId(), true);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data(payResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}