package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.application.facade.BookFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.service.BookService;
import io.hhplus.concertbook.domain.service.OutboxService;
import io.hhplus.concertbook.domain.service.SeatService;
import io.hhplus.concertbook.domain.service.TokenService;
import io.hhplus.concertbook.event.Book.BookEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;


public class BookFacadeTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private SeatService seatService;

    @Mock
    private BookService bookService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private BookFacade bookFacade;

    @Mock
    private OutboxService outboxService;

    @Mock
    private BookEvent bookEvent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("파사드 테스트: 예약 성공")
    public void testBook() throws Exception {
        String token = "validToken";
        long seatId = 1L;
        WaitTokenEntity waitToken = new WaitTokenEntity();
        SeatEntity seat = new SeatEntity();
        UserEntity user = new UserEntity();
        BookEntity book = new BookEntity();
        book.setBookId(123L);

        Mockito.when(tokenService.validateToken(token, ApiNo.BOOK)).thenReturn(waitToken);
        Mockito.when(seatService.findAndLockSeat(seatId)).thenReturn(seat);
        Mockito.when(tokenService.findUserByToken(token)).thenReturn(user);
        Mockito.when(bookService.createBooking(user, seat)).thenReturn(book);
        Mockito.when(outboxService.bookOutboxService(book,seat,waitToken)).thenReturn(bookEvent);

        long bookId = bookFacade.book(token, seatId);

        Assertions.assertEquals(123L, bookId);
        Mockito.verify(tokenService).validateToken(token, ApiNo.BOOK);
        Mockito.verify(seatService).findAndLockSeat(seatId);
        Mockito.verify(tokenService).findUserByToken(token);
        Mockito.verify(bookService).createBooking(user, seat);
    }
}
