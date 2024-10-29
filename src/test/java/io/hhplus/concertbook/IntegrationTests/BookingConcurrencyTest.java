package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.application.facade.BookFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.tool.RepositoryClean;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.ConcertService;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = ConcertBookApp.class)
public class BookingConcurrencyTest {

    @Autowired
    private MoneyService moneyService;

//    @Autowired
//    private ConcertService concertService;

    @Autowired
    private BookFacade bookFacade;

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
    private RepositoryClean repositoryClean;

    @BeforeEach
//    @Transactional
    public void setUp() {
        repositoryClean.cleanRepository();

        String userName = "testUser";
        UserEntity user = new UserEntity();
        user.setUserName(userName);
        userRepository.save(user);

        WaitTokenEntity waitToken = new WaitTokenEntity();
        waitToken.setToken("testToken");
        waitToken.setUser(user);
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitToken.setServiceCd(ApiNo.BOOK);
        waitTokenRepository.save(waitToken);



    }

    @Test
//    @Transactional
    public void testConcurrentBooking() throws Exception {
        String token = "testToken";
//        long seatId = 1L;
        int threadCount = 10;

        SeatEntity seat = new SeatEntity();
        seat.setSeatId(1L);
        seat.setUse(false);
        SeatEntity savedSeat = seatRepository.save(seat);

        long seatId = savedSeat.getSeatId();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    bookFacade.book(token, seatId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        SeatEntity seat1 = seatRepository.findById(seatId).get();
        Assertions.assertNotNull(seat1);
        Assertions.assertTrue(seat1.isUse());
        Assertions.assertEquals(1L,bookRepository.countBySeat_SeatId(seatId));

//        Assertions.assertThrows(Exception.class, () -> {
//            if (seat.isUse()) {
//                throw new Exception("좌석점유중");
//            }
//        });
    }
}