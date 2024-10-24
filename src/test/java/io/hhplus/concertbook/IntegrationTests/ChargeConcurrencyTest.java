package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.tool.RepositoryClean;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = ConcertBookApp.class)
public class ChargeConcurrencyTest {

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RepositoryClean repositoryClean;

    @BeforeEach
    public void clean() {
        repositoryClean.cleanRepository();
    }

    @Test
//    @Transactional
    public void testConcurrentCharge() throws Exception {
        String userName = "testUser";
        Long initialAmount = 1000L;
        Long chargeAmount = 100L;
        int threadCount = 10;

        // 유저와 지갑 초기화
        UserEntity user = new UserEntity();
        user.setUserName(userName);
        userRepository.save(user);

        WalletEntity wallet = new WalletEntity();
        wallet.setUser(user);
        wallet.setAmount(initialAmount);
        walletRepository.save(wallet);

        // 동시성 테스트를 위한 쓰레드 풀과 CountDownLatch 설정
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    moneyService.charge(userName, chargeAmount);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 최종 금액 검증
        WalletEntity updatedWallet = walletRepository.findByUser_UserId(user.getUserId());
        Assertions.assertEquals(initialAmount + chargeAmount * threadCount, updatedWallet.getAmount());
    }
}
