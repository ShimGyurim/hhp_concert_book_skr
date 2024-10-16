package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.common.exception.AmtMinusException;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.service.MoneyService;
import io.hhplus.concertbook.presentation.HttpDto.request.BalanceReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.RechargeReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.BalanceResDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.HttpDto.response.RechargeResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/money")
@Tag(name = "Money Management", description = "잔액/충전 api")
public class MoneyController {

    @Autowired
    MoneyService moneyService;

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

    @GetMapping("/balance")
    @Operation(summary = "잔액조회", description = "잔액조회")
    public ResponseEntity<CommonResponse<Object>> getBalance(
            @RequestParam(value="username") String userName) throws Exception {

        if(userName==null) {
            throw new Exception("유저 없음");
        }

        Long balance = moneyService.getBalance(userName);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(balance)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
