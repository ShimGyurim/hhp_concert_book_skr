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
import io.hhplus.concertbook.event.Book.BookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public long book(String token, long seatId) throws Exception {
        WaitTokenEntity waitToken = tokenService.validateToken(token, ApiNo.BOOK);
        SeatEntity seat = seatService.findAndLockSeat(seatId);
        UserEntity user = tokenService.findUserByToken(token);
        BookEntity book = bookService.createBooking(user, seat);

        eventPublisher.publishEvent(new BookEvent(book, waitToken));

        tokenService.endProcess(waitToken);
        return book.getBookId();
    }
}
