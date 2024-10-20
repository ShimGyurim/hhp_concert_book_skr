package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ConcertBookApp.class)
public class MoneyIntegTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private MoneyService moneyService;

    @Test
    @Transactional
    @DisplayName("잔액조회성공")
    public void testGetBalance_UserExists_WalletExists() throws Exception {
        // Given
        UserEntity user = new UserEntity();
        user.setUserName("testUser");
        userRepository.save(user);

        WalletEntity wallet = WalletEntity.builder().build();
        wallet.setUser(user);
        wallet.setAmount(100L);
        walletRepository.save(wallet);

        // When
        long balance = moneyService.getBalance("testUser");

        // Then
        Assertions.assertEquals(100L, balance);
    }

    @Test
    @Transactional
    @DisplayName("잔액충전 성공")
    public void testCharge_UserExists() throws Exception {
        // Given
        UserEntity user = new UserEntity();
        user.setUserName("testUser");
        userRepository.save(user);

        WalletEntity wallet = WalletEntity.builder().build();
        wallet.setUser(user);
        wallet.setAmount(100L);
        walletRepository.save(wallet);

        // When
        long newBalance = moneyService.charge("testUser", 50L);

        // Then
        Assertions.assertEquals(150L, newBalance);
    }

}
