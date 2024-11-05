package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.application.facade.PayFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.tool.RepositoryClean;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.ConcertService;
import io.hhplus.concertbook.domain.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConcertBookApp.class)
public class PayConcurrencyTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PayFacade payFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WaitTokenRepository waitTokenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertItemRepository concertItemRepository;

    @Autowired
    private RepositoryClean repositoryClean;

    @BeforeEach
    @Transactional
    public void setUp() {
        repositoryClean.cleanRepository();

        String userLoginId = "testUser";
        UserEntity user = new UserEntity();
        user.setUserLoginId(userLoginId);
        userRepository.save(user);

        WaitTokenEntity waitToken = new WaitTokenEntity();
        waitToken.setToken("testToken");
        waitToken.setUser(user);
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitToken.setServiceCd(ApiNo.PAYMENT);
        waitTokenRepository.save(waitToken);

        SeatEntity seat = new SeatEntity();
        seat.setUse(false);
        seatRepository.save(seat);

        ConcertEntity concert = new ConcertEntity();
        concert.setFee(100L);
        concertRepository.save(concert);

        ConcertItemEntity concertItem = new ConcertItemEntity();
        concertItem.setConcert(concert);
        concertItemRepository.save(concertItem);
        seat.setConcertItem(concertItem);
        seatRepository.save(seat);

        BookEntity book = new BookEntity();
        book.setUser(user);
        book.setSeat(seat);
        book.setStatusCd(BookStatus.PREPAYMENT);
        book.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        book.setUpdatedAt(book.getCreatedAt());
        bookRepository.save(book);

        WalletEntity wallet = new WalletEntity();
        wallet.setUser(user);
        wallet.setAmount(200L);
        walletRepository.save(wallet);
    }

    @Test
    public void testConcurrentPayment() throws Exception {
        String token = "testToken";
        Long bookId = bookRepository.findAll().get(0).getBookId();
        Long walletId = walletRepository.findAll().get(0).getWalletId();
        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    payFacade.pay(token, bookId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        BookEntity book = bookRepository.findById(bookId).orElse(null);
        assertNotNull(book);
        assertTrue(BookStatus.PAID.equals(book.getStatusCd()));
        assertEquals(200L-100L,walletRepository.findById(walletId).get().getAmount());
        Assertions.assertEquals(1L,paymentRepository.countByBook_BookId(bookId));
    }
}
