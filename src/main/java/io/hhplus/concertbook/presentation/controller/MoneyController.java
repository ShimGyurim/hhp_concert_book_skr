package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.common.exception.AmtMinusException;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.presentation.HttpDto.request.BalanceReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.RechargeReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.BalanceResDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.HttpDto.response.RechargeResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/money")
@Tag(name = "잔액/충전 api", description = "잔액/충전 api")
public class MoneyController {

    @PostMapping("/recharge")
    @Operation(summary = "충전", description = "충전")
    public ResponseEntity<CommonResponse<Object>> rechargeBalance(
            @RequestBody RechargeReqDto rechargeReqDto
            ) throws AmtMinusException, NoTokenException {
        if(rechargeReqDto.getChargeAmount() <0) {
            throw new AmtMinusException("음수 충전");
        }

        if(rechargeReqDto.getToken() == null) {
            throw new NoTokenException("토큰 없음");
        }

        Long curAmount = 50L;
        curAmount += rechargeReqDto.getChargeAmount();

        RechargeResDto rechargeResDto = new RechargeResDto(true,curAmount);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(rechargeResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/balance")
    @Operation(summary = "잔액조회", description = "잔액조회")
    public ResponseEntity<CommonResponse<Object>> getBalance(
            @RequestBody BalanceReqDto balanceReqDto) throws NoTokenException {

        if(balanceReqDto.getToken()==null) {
            throw new NoTokenException("토큰 없음");
        }

        BalanceResDto balanceResDto = new BalanceResDto(true);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(balanceResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
