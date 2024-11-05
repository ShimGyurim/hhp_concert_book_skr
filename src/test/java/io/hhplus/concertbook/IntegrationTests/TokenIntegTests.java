package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
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
import io.hhplus.concertbook.tool.RepositoryClean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@SpringBootTest(classes = ConcertBookApp.class)
public class TokenIntegTests {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WaitTokenRepository waitTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepositoryClean repositoryClean;

    @BeforeEach
    public void setUp() {
        repositoryClean.cleanRepository();
        // 초기 데이터 설정
        UserEntity user = new UserEntity();
        user.setUserLoginId("testUser");
        userRepository.save(user);
    }

    @Test
    @Transactional
    @DisplayName("토큰 새로 생성")
    public void testGetToken_NewToken() throws Exception {

        TokenDto tokenInDto = new TokenDto();
        tokenInDto.setUserLoginId("testUser");
        tokenInDto.setApiNo(ApiNo.BOOK);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserLoginId());
        Assertions.assertEquals(ApiNo.BOOK, result.getApiNo());
        Assertions.assertNotNull(result.getToken());
        Assertions.assertEquals(0, result.getWaitNo());
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());

        WaitTokenEntity savedToken = waitTokenRepository.findByToken(result.getToken());
        Assertions.assertNotNull(savedToken);
        Assertions.assertEquals("testUser", savedToken.getUser().getUserLoginId());
        Assertions.assertEquals(ApiNo.BOOK, savedToken.getServiceCd());
    }

    @Test
    @Transactional
    @DisplayName("토큰: 이미 유효한 토큰 가진 사용자")
    public void testGetToken_ExistingToken() throws Exception {
        UserEntity user = userRepository.findByUserLoginId("testUser");

        WaitTokenEntity existingToken = new WaitTokenEntity();
        existingToken.setToken("existingToken");
        existingToken.setUser(user);
        existingToken.setServiceCd(ApiNo.PAYMENT);
        existingToken.setStatusCd(WaitStatus.WAIT);
        existingToken.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        waitTokenRepository.save(existingToken);

        TokenDto tokenInDto = new TokenDto();
        tokenInDto.setUserLoginId("testUser");
        tokenInDto.setApiNo(ApiNo.PAYMENT);

        TokenDto result = tokenService.getToken(tokenInDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUserLoginId());
        Assertions.assertEquals(ApiNo.PAYMENT, result.getApiNo());
        Assertions.assertEquals("existingToken", result.getToken());
        Assertions.assertEquals(0, result.getWaitNo());
        Assertions.assertEquals(WaitStatus.PROCESS, result.getWaitStatus());

        WaitTokenEntity updatedToken = waitTokenRepository.findByToken("existingToken");
        Assertions.assertEquals(WaitStatus.PROCESS, updatedToken.getStatusCd());
    }

    @Test
    @Transactional
    @DisplayName("토큰: 유효한 유저 못찾음")
    public void testGetToken_UserNotFound() {
        TokenDto tokenInDto = new TokenDto();
        tokenInDto.setUserLoginId("nonexistentUser");
        tokenInDto.setApiNo(ApiNo.BOOK);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            tokenService.getToken(tokenInDto);
        });

        Assertions.assertEquals(ErrorCode.USER_ERROR, exception.getErrorCode());
    }
}
