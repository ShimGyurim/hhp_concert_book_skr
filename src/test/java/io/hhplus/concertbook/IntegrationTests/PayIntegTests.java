package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.application.facade.PayFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.repository.RedisRepository;
import io.hhplus.concertbook.tool.RepositoryClean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ConcertBookApp.class)
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
    ConcertRepository concertRepository;

    @Autowired
    ConcertItemRepository concertItemRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    PayFacade payFacade;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private RepositoryClean repositoryClean;

    private UserEntity user;
    private BookEntity book;
    private WalletEntity wallet;
    private WaitTokenEntity waitToken;

    @BeforeEach
    public void setUp() {
        repositoryClean.cleanRepository();
        // 사용자 생성
        user = new UserEntity();
        user.setUserLoginId("testUser");
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
        waitTokenRepository.save(waitToken);

        redisRepository.activeEnqueue(ApiNo.PAYMENT.toString(),"testToken");
    }

    @Test
    @Transactional
    @DisplayName("결제성공")
    public void testPay_Success() throws Exception {
        // Given
        waitTokenRepository.save(waitToken);

        // When
        boolean result = payFacade.pay("testToken", book.getBookId());

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
        Assertions.assertThrows(CustomException.class, () -> payFacade.pay(null, book.getBookId()));
    }
}
