package io.hhplus.concertbook.UnitTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.concertbook.application.facade.PayFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

public class PayFacadeTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private ConcertService concertService;

    @Mock
    private UserService userService;

    @Mock
    private MoneyService moneyService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private BookService bookService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PayFacade payFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("결제 파사드 : 성공")
    public void testPay() throws Exception {
        String token = "validToken";
        Long bookId = 1L;
        WaitTokenEntity waitToken = new WaitTokenEntity();
        BookEntity book = new BookEntity();
        UserEntity userBook = new UserEntity();
        UserEntity userToken = new UserEntity();
        SeatEntity seat = new SeatEntity();
        ConcertEntity concert = new ConcertEntity();
        WalletEntity wallet = new WalletEntity();

        waitToken.setUser(userToken);
        book.setUser(userBook);
        book.setSeat(seat);
        seat.setConcertItem(new ConcertItemEntity());
        seat.getConcertItem().setConcert(concert);
        concert.setFee(1000L);

        when(tokenService.validateToken(token, ApiNo.PAYMENT)).thenReturn(waitToken);
        when(concertService.findAndLockBook(bookId)).thenReturn(book);
        when(moneyService.findAndLockWallet(userBook.getUserId())).thenReturn(wallet);

        boolean result = payFacade.pay(token, bookId);

        assertTrue(result);
        verify(tokenService).validateToken(token, ApiNo.PAYMENT);
        verify(concertService).findAndLockBook(bookId);
        verify(userService).validateUser(userBook, userToken);
        verify(moneyService).findAndLockWallet(userBook.getUserId());
        verify(moneyService).deductAmount(wallet, 1000L);
        verify(paymentService).createPayment(book);
        verify(bookService).updateBookStatus(book, BookStatus.PAID);
    }
}

