package io.hhplus.concertbook.application.facade;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.service.BookService;
import io.hhplus.concertbook.domain.service.SeatService;
import io.hhplus.concertbook.domain.service.TokenService;
import io.hhplus.concertbook.domain.service.UserService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookFacade {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @Transactional
    public long book(String token, long seatId) throws Exception {
        WaitTokenEntity waitToken = tokenService.validateToken(token, ApiNo.BOOK);
        SeatEntity seat = seatService.findAndLockSeat(seatId);
        UserEntity user = tokenService.findUserByToken(token);
        BookEntity book = bookService.createBooking(user, seat);
        tokenService.endProcess(waitToken);
        return book.getBookId();
    }
}
