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
import org.mockito.*;
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
        MockitoAnnotations.openMocks(this);
        tokenInDto = new TokenDto();
        tokenInDto.setUserLoginId("testUser");
        tokenInDto.setApiNo(ApiNo.BOOK);

        user = new UserEntity();
        user.setUserLoginId("testUser");

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
        Mockito.when(waitTokenRepository.findByUser_UserLoginIdAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepository.findByUserLoginId("testUser")).thenReturn(user);
        Mockito.when(waitTokenRepository.save(ArgumentMatchers.any(WaitTokenEntity.class))).thenReturn(entity);
        Mockito.when(waitTokenRepository.countStatusToken(ArgumentMatchers.any(ApiNo.class),ArgumentMatchers.any(WaitStatus.class))).thenReturn(0L);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserLoginId());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertNotNull(result.getToken());
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());
    }

    @Test
    @DisplayName("기존 토큰 있는경우")
    public void testGetToken_ExistingToken() throws Exception {
        Mockito.when(waitTokenRepository.findByUser_UserLoginIdAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(entity);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserLoginId());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertTrue( result.getToken().contains(result.getUserLoginId()) );
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());
    }

    @Test
    @DisplayName("토큰검증 : 유저못찾음")
    public void testGetToken_UserNotFound() {
        Mockito.when(waitTokenRepository.findByUser_UserLoginIdAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepository.findByUserLoginId("testUser")).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            tokenService.getToken(tokenInDto);
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰검증 : 성공")
    public void testValidateToken_Success() throws CustomException {
        String token = "validToken";
        ApiNo apiNo = ApiNo.PAYMENT;
        WaitTokenEntity waitToken = new WaitTokenEntity();
        waitToken.setStatusCd(WaitStatus.PROCESS);
        waitToken.setServiceCd(apiNo);

        Mockito.when(waitTokenRepository.findByToken(token)).thenReturn(waitToken);

        WaitTokenEntity result = tokenService.validateToken(token, apiNo);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(waitToken, result);
    }

    @Test
    @DisplayName("토큰검증 : 토큰정보없음")
    public void testValidateToken_TokenNull() {
        String token = null;
        ApiNo apiNo = ApiNo.PAYMENT;

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            tokenService.validateToken(token, apiNo);
        });

        Assertions.assertEquals(ErrorCode.TOKEN_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰으로 유저찾기 : 성공")
    public void testFindUserByToken_Success() throws CustomException {
        String token = "validToken";
        UserEntity user = new UserEntity();

        Mockito.when(waitTokenRepository.findUserinfoByToken(token)).thenReturn(user);

        UserEntity result = tokenService.findUserByToken(token);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);
    }

    @Test
    @DisplayName("토큰으로 유저찾기 : 유저 못찾음")
    public void testFindUserByToken_UserNotFound() {
        String token = "invalidToken";

        Mockito.when(waitTokenRepository.findUserinfoByToken(token)).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            tokenService.findUserByToken(token);
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }
}
