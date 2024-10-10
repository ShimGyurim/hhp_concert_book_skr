package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.presentation.HttpDto.request.TokenReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.HttpDto.response.TokenResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Token Management", description = "토큰 발급")
public class TokenController {

    @PostMapping("/token")
    @Operation(summary = "토큰발급", description = "토큰을 사용자에게 발급")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> issueUserToken(
                @RequestBody
                @Parameter(required = true, description = "토큰입력")
                        TokenReqDto tokenReqDto) throws Exception {

        if(tokenReqDto.getUserId() == null) {
            throw new NoIdException("아이디가 없습니다.");
        }

        //TODO : 토큰 만료 확인 로직은 service 에서

        String userId = tokenReqDto.getUserId();
        String nowDt = "20240930113000";
        String token = userId+nowDt;
        int waitNum = 1;
        TokenResDto tokenResDto = new TokenResDto(waitNum,token,userId);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(tokenResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
