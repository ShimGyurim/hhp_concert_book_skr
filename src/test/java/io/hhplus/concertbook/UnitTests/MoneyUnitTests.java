package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class MoneyUnitTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private MoneyService moneyService;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저못찾음")
    public void testGetBalance_UserNotFound() {
        Mockito.when(userRepository.findByUserName("nouser")).thenReturn(null);
//        Mockito.when(walletRepo.findByUser_UserId(ArgumentMatchers.any(Long.class))).thenReturn(new WalletEntity());

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            moneyService.getBalance("nouser");
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("지갑정보못찾음")
    public void testGetBalance_WalletNotFound() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        Mockito.when(userRepository.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepository.findByUser_UserId(1L)).thenReturn(null);

        long balance = moneyService.getBalance("existingUser");

        Assertions.assertEquals(0L, balance);
        Mockito.verify(walletRepository, Mockito.times(1)).save(ArgumentMatchers.any(WalletEntity.class));
    }

    @Test
    @DisplayName("지갑정보찾음")
    public void testGetBalance_WalletFound() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        WalletEntity wallet = new WalletEntity();
        wallet.setAmount(100L);
        Mockito.when(userRepository.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepository.findByUser_UserId(1L)).thenReturn(wallet);

        long balance = moneyService.getBalance("existingUser");

        Assertions.assertEquals(100L, balance);
    }

    @Test
    @DisplayName("비정상 충전금액")
    public void testCharge_InvalidAmount() {
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            moneyService.charge("user", -100L);
        });
        Assertions.assertEquals(ErrorCode.CHARGE_INPUT_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 못찾음")
    public void testCharge_UserNotFound() {
        Mockito.when(userRepository.findByUserName("nonexistentUser")).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            moneyService.charge("nonexistentUser", 100L);
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("충전 성공")
    public void testCharge_Success() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        WalletEntity wallet = new WalletEntity();
        wallet.setAmount(100L);

        Mockito.when(userRepository.findByUserName("existingUser")).thenReturn(user);
        Mockito.when(walletRepository.findByUser_UserIdWithLock(1L)).thenReturn(wallet);

        long newBalance = moneyService.charge("existingUser", 50L);

        Assertions.assertEquals(150L, newBalance);
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet);
    }





    @Test
    @DisplayName("잔액 정보 (락 객체) : 성공")
    public void testFindAndLockWallet_Success() throws CustomException {
        Long userId = 1L;
        WalletEntity wallet = new WalletEntity();

        Mockito.when(walletRepository.findByUser_UserIdWithLock(userId)).thenReturn(wallet);

        WalletEntity result = moneyService.findAndLockWallet(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(wallet, result);
        Mockito.verify(walletRepository).findByUser_UserIdWithLock(userId);
    }

    @Test
    @DisplayName("예약 정보 (락 객체) : 잔액정보 못찾음")
    public void testFindAndLockWallet_WalletNotFound() {
        Long userId = 1L;

        Mockito.when(walletRepository.findByUser_UserIdWithLock(userId)).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            moneyService.findAndLockWallet(userId);
        });

        Assertions.assertEquals(ErrorCode.NO_WALLET, exception.getErrorCode());
    }
}
