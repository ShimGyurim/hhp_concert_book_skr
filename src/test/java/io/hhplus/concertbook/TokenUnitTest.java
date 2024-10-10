package io.hhplus.concertbook;

import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.presentation.HttpDto.request.TokenReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.controller.TokenController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TokenUnitTest {

    @InjectMocks
    private TokenController tokenController;

    @Test
    @DisplayName("토큰 발급 테스트")
    public void issueUserToken_success() throws Exception {
        // given
        TokenReqDto reqDto = new TokenReqDto("testUser");

        // when
        ResponseEntity<CommonResponse<Object>> response = tokenController.issueUserToken(reqDto);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("토큰 발급 실패 테스트")
    public void issueUserToken_fail_nullUserId() throws Exception {
        // given
        TokenReqDto reqDto = new TokenReqDto(null);

        // when, then
        Assertions.assertThrows(NoIdException.class, () -> tokenController.issueUserToken(reqDto));
    }
}
