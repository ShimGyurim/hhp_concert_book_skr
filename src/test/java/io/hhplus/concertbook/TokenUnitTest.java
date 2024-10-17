package io.hhplus.concertbook;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.common.exception.NoUserException;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import io.hhplus.concertbook.domain.service.TokenService;
import io.hhplus.concertbook.presentation.HttpDto.request.TokenReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.controller.TokenController;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class TokenUnitTest {

    @Mock
    private WaitTokenRepo waitTokenRepo;

    @Mock
    private UserRepo userRepo;

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
        Mockito.when(waitTokenRepo.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepo.findByUserName("testUser")).thenReturn(user);
        Mockito.when(waitTokenRepo.save(ArgumentMatchers.any(WaitTokenEntity.class))).thenReturn(entity);
        Mockito.when(waitTokenRepo.countStatusToken(ArgumentMatchers.any(ApiNo.class),ArgumentMatchers.any(WaitStatus.class))).thenReturn(0L);

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
        Mockito.when(waitTokenRepo.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(entity);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserName());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertTrue( result.getToken().contains(result.getUserName()) );
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());
    }

    @Test
    public void testGetToken_UserNotFound() {
        Mockito.when(waitTokenRepo.findByUser_UserNameAndServiceCd("testUser", ApiNo.BOOK)).thenReturn(null);
        Mockito.when(userRepo.findByUserName("testUser")).thenReturn(null);

        Exception exception = Assertions.assertThrows(NoUserException.class, () -> {
            tokenService.getToken(tokenInDto);
        });

        Assertions.assertEquals("사용자없음", exception.getMessage());
    }
}
