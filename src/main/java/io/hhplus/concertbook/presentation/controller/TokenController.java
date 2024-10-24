package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.service.TokenService;
import io.hhplus.concertbook.presentation.HttpDto.request.TokenReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.HttpDto.response.TokenResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Token Management", description = "토큰 발급")
public class TokenController {

    @Autowired
    TokenService tokenService;

    @PostMapping("/token")
    @Operation(summary = "토큰발급", description = "토큰을 사용자에게 발급")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> issueUserToken(
                @RequestBody
                @Parameter(required = true, description = "토큰입력")
                        TokenReqDto tokenReqDto) throws Exception {

        if(tokenReqDto.getUserName() == null) {
            throw new NoIdException("아이디가 없습니다.");
        }
        if(tokenReqDto.getApiServiceName() == null) {
            throw new NoIdException("대기요청 서비스 이름이 없습니다.");
        }

        TokenDto tokenInDto = new TokenDto();
        tokenInDto.setUserName(tokenReqDto.getUserName());
        if ("BOOK".equals(tokenReqDto.getApiServiceName()))
            tokenInDto.setApiNo(ApiNo.BOOK);
        else if("PAYMENT".equals(tokenReqDto.getApiServiceName()))
            tokenInDto.setApiNo(ApiNo.PAYMENT);

        TokenDto tokenOutDto = tokenService.getToken(tokenInDto);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(tokenOutDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
