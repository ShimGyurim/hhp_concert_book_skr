package io.hhplus.concertbook.IntegrationTests;


import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.application.facade.BookFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.dto.SeatDto;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.ConcertService;
import io.hhplus.concertbook.tool.RepositoryClean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = ConcertBookApp.class)
public class ConcertIntegTests {

    @Autowired
    private ConcertItemRepository concertItemRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private WaitTokenRepository waitTokenRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private RepositoryClean repositoryClean;

    @Autowired
    private BookFacade bookFacade;

    private ConcertItemEntity concertItem;
    private SeatEntity seat;
    private WaitTokenEntity waitToken;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        repositoryClean.cleanRepository();

        user = new UserEntity();
        user.setUserName("testUser");
        userRepository.save(user);

        // 콘서트 아이템 생성
        concertItem = new ConcertItemEntity();
        concertItem.setConcertD("20241015");
        concertItem.setAvailSeats(100);
        concertItemRepository.save(concertItem);

        // 좌석 생성
        seat = new SeatEntity();
        seat.setConcertItem(concertItem);
        seat.setSeatNo(10);
        seat.setUse(false);
        seatRepository.save(seat);

        // 대기 토큰 생성
        waitToken = new WaitTokenEntity();
        waitToken.setToken("testToken");
        waitToken.setUser(user);
        waitToken.setServiceCd(ApiNo.BOOK);
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitTokenRepository.save(waitToken);
    }

    @Test
    @Transactional
    @DisplayName("콘서트 스케줄 찾기 성공")
    public void testGetAvailSchedule() {
        // When
        List<ConcertScheduleDto> schedules = concertService.getAvailSchedule("20241015");

        // Then
        Assertions.assertNotNull(schedules);
        Assertions.assertFalse(schedules.isEmpty());
        Assertions.assertEquals(1, schedules.size());
        Assertions.assertEquals(concertItem.getConcertItemId(), schedules.get(0).getConcertItemId());
        Assertions.assertEquals(concertItem.getAvailSeats(), schedules.get(0).getAvaliSeats());
    }

    @Test
    @Transactional
    @DisplayName("좌석 찾기 성공")
    public void testGetSeats() {
        // When
        List<SeatDto> seats = concertService.getSeats(concertItem.getConcertItemId());

        // Then
        Assertions.assertNotNull(seats);
        Assertions.assertFalse(seats.isEmpty());
        Assertions.assertEquals(1, seats.size());
        Assertions.assertEquals(seat.getSeatId(), seats.get(0).getSeatId());
        Assertions.assertEquals(seat.getSeatNo(), seats.get(0).getSeatNo());
        Assertions.assertEquals(seat.isUse(), seats.get(0).isUse());
    }

    @Test
    @Transactional
    @DisplayName("좌석예약: 성공")
    public void testBook_Success() throws Exception {
        // When
        long bookId = bookFacade.book("testToken", seat.getSeatId());

        // Then
        Assertions.assertNotNull(bookId);
        BookEntity booked = bookRepository.findById(bookId).orElse(null);
        Assertions.assertNotNull(booked);
        Assertions.assertEquals(BookStatus.PREPAYMENT, booked.getStatusCd());
        Assertions.assertEquals(seat.getSeatId(), booked.getSeat().getSeatId());
        Assertions.assertTrue(seatRepository.findById(seat.getSeatId()).get().isUse());
    }

    @Test
    @Transactional
    @DisplayName("좌석예약: 토큰이없어 실패")
    public void testBook_NoToken() {
        // When & Then
        Assertions.assertThrows(CustomException.class, () -> bookFacade.book(null, seat.getSeatId()));
    }
}
