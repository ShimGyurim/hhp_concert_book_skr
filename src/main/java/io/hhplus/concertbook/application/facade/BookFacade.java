package io.hhplus.concertbook.application.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.domain.service.*;
import io.hhplus.concertbook.event.Book.BookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

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
    private OutboxService outboxService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public long book(String token, long seatId) throws Exception {
        WaitTokenEntity waitToken = tokenService.validateToken(token, ApiNo.BOOK);
        SeatEntity seat = seatService.findAndLockSeat(seatId);
        UserEntity user = tokenService.findUserByToken(token);
        BookEntity book = bookService.createBooking(user, seat);

        BookEvent bookEvent = outboxService.bookOutboxService(book,seat,waitToken);
        eventPublisher.publishEvent(bookEvent);

        return book.getBookId();
    }
}
