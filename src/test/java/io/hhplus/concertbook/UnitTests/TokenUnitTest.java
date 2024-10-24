package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import io.hhplus.concertbook.domain.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class TokenUnitTest {

    @Mock
    private WaitTokenRepository waitTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    TokenService tokenService;

    private TokenDto tokenInDto;
    private UserEntity user;
    private WaitTokenEntity entity;


    @BeforeEach
    public void setUp() {
        tokenInDto = new TokenDto();
        tokenInDto.setUserName("testUser");
        tokenInDto.setApiNo(ApiNo.BOOK);

        user = new UserEntity();
        user.setUserName("testUser");

        entity = new WaitTokenEntity();
        entity.setUser(user);
        entity.setServiceCd(ApiNo.BOOK);
        entity.setStatusCd(WaitStatus.WAIT);
        entity.setToken(user+""+entity.getUpdatedAt());
        entity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    @DisplayName("토큰이 없는경우")
    public void testGetToken_NewToken() throws Exception {
        Mockito.when(waitTokenRepository.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepository.findByUserName("testUser")).thenReturn(user);
        Mockito.when(waitTokenRepository.save(ArgumentMatchers.any(WaitTokenEntity.class))).thenReturn(entity);
        Mockito.when(waitTokenRepository.countStatusToken(ArgumentMatchers.any(ApiNo.class),ArgumentMatchers.any(WaitStatus.class))).thenReturn(0L);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserName());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertNotNull(result.getToken());
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());
    }

    @Test
    @DisplayName("기존 토큰 있는경우")
    public void testGetToken_ExistingToken() throws Exception {
        Mockito.when(waitTokenRepository.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(entity);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserName());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertTrue( result.getToken().contains(result.getUserName()) );
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());
    }

    @Test
    public void testGetToken_UserNotFound() {
        Mockito.when(waitTokenRepository.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepository.findByUserName("testUser")).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            tokenService.getToken(tokenInDto);
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }
}
