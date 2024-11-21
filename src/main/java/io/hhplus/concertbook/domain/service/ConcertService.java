package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.dto.SeatDto;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.ConcertItemRepository;
import io.hhplus.concertbook.domain.repository.SeatRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConcertService {

    @Autowired
    ConcertItemRepository concertItemRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    WaitQueueService waitQueueService;

    @Autowired
    BookRepository bookRepository;

    @Cacheable(value = "concertSchedule", key = "#scheduleDate", cacheManager = "concertCacheManager")
    public List<ConcertScheduleDto> getAvailSchedule(String scheduleDate){
        List<ConcertItemEntity> concertItems = concertItemRepository.findByConcertD(scheduleDate);

        log.info("CONCERT SCHEDULE NOT CACHED");
       List<ConcertScheduleDto> concertSchdules = concertItems.stream()
                .map(item -> {
                    long id = item.getConcertItemId();
                    int availSeats = concertItemRepository.countAvailSeats(id);
                    //TODO : getConcert 없으면 exception

                    ConcertScheduleDto concertScheduleDto = new ConcertScheduleDto();

                    concertScheduleDto.setConcertItemId(id);
                    concertScheduleDto.setAvaliSeats(availSeats);
                    return concertScheduleDto;
                })
                .collect(Collectors.toList());


        return concertSchdules;
    }

    public List<SeatDto> getSeats(long scheduleId){ //출력: 좌석id랑, 좌석 no 리스트
        List<SeatEntity> seatItems = seatRepository.findByConcertItem_ConcertItemId(scheduleId);

        List<SeatDto> seatNos = seatItems.stream()
                .map(item -> {
                    SeatDto dto = new SeatDto();
                    dto.setSeatId(item.getSeatId());
                    dto.setSeatNo(item.getSeatNo());
                    dto.setUse(item.isUse());
                    return dto;
                })
                .collect(Collectors.toList());

        return seatNos;
    }

    public BookEntity findAndLockBook(Long bookId) throws CustomException {
        BookEntity book = bookRepository.findByIdWithLock(bookId);
        if (book == null) {
            throw new CustomException(ErrorCode.BOOK_ERROR);
        }
        if (!BookStatus.PREPAYMENT.equals(book.getStatusCd())) {
            throw new CustomException(ErrorCode.NO_PAY);
        }
        return book;
    }

}
