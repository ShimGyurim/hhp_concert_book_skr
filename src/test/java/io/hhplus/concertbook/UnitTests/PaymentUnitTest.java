package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaymentUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private WaitTokenRepository waitTokenRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @DisplayName("토큰없음")
    public void testPay_NoToken() {
        Exception exception = Assertions.assertThrows(NoTokenException.class, () -> {
            paymentService.pay(null, 1L);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    @DisplayName("예약정보 못찾음")
    public void testPay_BookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            paymentService.pay("validToken", 1L);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    @DisplayName("결제할 항목없음")
    public void testPay_BookNotPrepayment() {
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PAID);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            paymentService.pay("validToken", 1L);
        });

        Assertions.assertEquals("결제할 항목없음", exception.getMessage());
    }

    @Test
    @DisplayName("토큰못찾음")
    public void testPay_TokenNotFound() {
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(waitTokenRepository.findByToken("invalidToken")).thenReturn(null);

        Exception exception = Assertions.assertThrows(NoTokenException.class, () -> {
            paymentService.pay("invalidToken", 1L);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    @DisplayName("토큰만료")
    public void testPay_TokenExpired() {
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        WaitTokenEntity waitToken = new WaitTokenEntity();
        waitToken.setStatusCd(WaitStatus.EXPIRED);
        Mockito.when(waitTokenRepository.findByToken("expiredToken")).thenReturn(waitToken);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            paymentService.pay("expiredToken", 1L);
        });

        Assertions.assertEquals("토큰만료", exception.getMessage());
    }

    @Test
    @DisplayName("결제 성공")
    public void testPay_Success() throws Exception {
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        book.setUser(user);
        SeatEntity seat = new SeatEntity();
        ConcertItemEntity concertItem = new ConcertItemEntity();
        concertItem.setConcert(new ConcertEntity());
        seat.setConcertItem(concertItem);
        book.setSeat(seat);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        WaitTokenEntity waitToken = new WaitTokenEntity();
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitToken.setServiceCd(ApiNo.PAYMENT);
        waitToken.setUser(user);
        Mockito.when(waitTokenRepository.findByToken("validToken")).thenReturn(waitToken);

        WalletEntity wallet = new WalletEntity();
        wallet.setAmount(1000L);
        Mockito.when(walletRepository.findByUser_UserId(1L)).thenReturn(wallet);

//        SeatEntity seatAnother = new SeatEntity();
        ConcertEntity concert = new ConcertEntity();
        concert.setFee(500L);
        ConcertItemEntity concertItem1 = new ConcertItemEntity();
        concertItem1.setConcert(concert);
        SeatEntity seat1 = new SeatEntity();
        seat1.setConcertItem(concertItem1);
        book.setSeat(seat1);

        boolean result = paymentService.pay("validToken", 1L);

        Assertions.assertTrue(result);
        Assertions.assertEquals(500L, wallet.getAmount());
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(ArgumentMatchers.any(PaymentEntity.class));
        Mockito.verify(waitTokenRepository, Mockito.times(1)).save(waitToken);
    }
}
