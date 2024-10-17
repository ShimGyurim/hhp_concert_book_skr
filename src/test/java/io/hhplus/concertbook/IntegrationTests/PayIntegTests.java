package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PayIntegTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WaitTokenRepository waitTokenRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    ConcertItemRepository concertItemRepository;

    @Autowired
    SeatRepository seatRepository;

    private UserEntity user;
    private BookEntity book;
    private WalletEntity wallet;
    private WaitTokenEntity waitToken;

    @BeforeEach
    public void setUp() {
        // 사용자 생성
        user = new UserEntity();
        user.setUserName("testUser");
        userRepository.save(user);

        // 지갑 생성
        wallet = new WalletEntity();
        wallet.setUser(user);
        wallet.setAmount(1000L);
        walletRepository.save(wallet);

        // 책 생성
        book = new BookEntity();
        book.setUser(user);
        ConcertEntity concertEntity = new ConcertEntity();
        concertEntity.setFee(1000L);
        concertRepository.save(concertEntity);

        ConcertItemEntity concertItemEntity = new ConcertItemEntity();
        concertItemEntity.setConcert(concertEntity);
        concertItemRepository.save(concertItemEntity);


        SeatEntity seat = new SeatEntity();
        seat.setConcertItem(concertItemEntity);
        seatRepository.save(seat);
        book.setSeat(seat);
        book.setStatusCd(BookStatus.PREPAYMENT);
        bookRepository.save(book);

        // 대기 토큰 생성
        waitToken = new WaitTokenEntity();
        waitToken.setToken("testToken");
        waitToken.setUser(user);
        waitToken.setServiceCd(ApiNo.PAYMENT);
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitTokenRepository.save(waitToken);
    }

    @Test
    @Transactional
    @DisplayName("결제성공")
    public void testPay_Success() throws Exception {
        // Given
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitTokenRepository.save(waitToken);

        // When
        boolean result = paymentService.pay("testToken", book.getBookId());

        // Then
        Assertions.assertTrue(result);
        Assertions.assertEquals(BookStatus.PAID, bookRepository.findById(book.getBookId()).get().getStatusCd());
        Assertions.assertEquals(0L, walletRepository.findByUser_UserId(user.getUserId()).getAmount());
    }

    @Test
    @Transactional
    @DisplayName("결제: 토큰없음 에러")
    public void testPay_NoToken() {
        // When & Then
        Assertions.assertThrows(NoTokenException.class, () -> paymentService.pay(null, book.getBookId()));
    }
}
