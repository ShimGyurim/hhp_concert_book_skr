package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.application.facade.MoneyFacade;
import io.hhplus.concertbook.tool.RepositoryClean;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(classes = ConcertBookApp.class)
public class ChargeConcurrencyTest {

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private MoneyFacade moneyFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RepositoryClean repositoryClean;

    @Autowired
    private RedissonClient redissonClient;

    @BeforeEach
    public void clean() {
        repositoryClean.cleanRepository();
    }

    @Test
    @DisplayName("분산락(심플락) 및 낙관적락 적용 : 성공 케이스만 카운트해서 검증")
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

        AtomicLong successCnt = new AtomicLong(0);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    moneyFacade.chargeWithRedisLock(userName, chargeAmount);
                    successCnt.incrementAndGet(); //성공한 케이스만 횟수 증가
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
        Assertions.assertEquals(initialAmount + chargeAmount * successCnt.get(), updatedWallet.getAmount());
    }
}
