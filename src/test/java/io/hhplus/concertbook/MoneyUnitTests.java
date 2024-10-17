package io.hhplus.concertbook;

import io.hhplus.concertbook.common.exception.AmtMinusException;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.common.exception.NoUserException;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WalletRepo;
import io.hhplus.concertbook.domain.service.MoneyService;
import io.hhplus.concertbook.presentation.HttpDto.request.BalanceReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.RechargeReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.controller.MoneyController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class MoneyUnitTests {
    @Mock
    private UserRepo userRepo;

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private MoneyService moneyService;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @DisplayName("유저못찾음")
    public void testGetBalance_UserNotFound() {
        Mockito.when(userRepo.findByUserName("nouser")).thenReturn(null);
//        Mockito.when(walletRepo.findByUser_UserId(ArgumentMatchers.any(Long.class))).thenReturn(new WalletEntity());

        Exception exception = Assertions.assertThrows(NoUserException.class, () -> {
            moneyService.getBalance("nouser");
        });

        Assertions.assertEquals("유저정보없음", exception.getMessage());
    }

    @Test
    @DisplayName("지갑정보못찾음")
    public void testGetBalance_WalletNotFound() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        Mockito.when(userRepo.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepo.findByUser_UserId(1L)).thenReturn(null);

        long balance = moneyService.getBalance("existingUser");

        Assertions.assertEquals(0L, balance);
        Mockito.verify(walletRepo, Mockito.times(1)).save(ArgumentMatchers.any(WalletEntity.class));
    }

    @Test
    @DisplayName("지갑정보찾음")
    public void testGetBalance_WalletFound() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        WalletEntity wallet = new WalletEntity();
        wallet.setAmount(100L);
        Mockito.when(userRepo.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepo.findByUser_UserId(1L)).thenReturn(wallet);

        long balance = moneyService.getBalance("existingUser");

        Assertions.assertEquals(100L, balance);
    }

    @Test
    @DisplayName("비정상 충전금액")
    public void testCharge_InvalidAmount() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            moneyService.charge("user", -100L);
        });

        Assertions.assertEquals("충전금액 이상", exception.getMessage());
    }

    @Test
    @DisplayName("유저 못찾음")
    public void testCharge_UserNotFound() {
        Mockito.when(userRepo.findByUserName("nonexistentUser")).thenReturn(null);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            moneyService.charge("nonexistentUser", 100L);
        });

        Assertions.assertEquals("유저 없음", exception.getMessage());
    }

    @Test
    @DisplayName("충전 성공")
    public void testCharge_Success() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        WalletEntity wallet = new WalletEntity();
        wallet.setAmount(100L);

        Mockito.when(userRepo.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepo.findByUser_UserId(1L)).thenReturn(wallet);

        long newBalance = moneyService.charge("existingUser", 50L);

        Assertions.assertEquals(150L, newBalance);
        Mockito.verify(walletRepo, Mockito.times(1)).save(wallet);
    }    
}
