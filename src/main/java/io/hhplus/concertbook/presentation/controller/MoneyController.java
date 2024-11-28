package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.application.facade.MoneyFacade;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.service.MoneyService;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
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

    @Autowired
    MoneyFacade moneyFacade;

    @GetMapping("/charge")
    @Operation(summary = "충전", description = "충전")
    public ResponseEntity<CommonResponse<Object>> chargeBalance(
            @RequestParam(value = "username") String userLoginId,
            @RequestParam(value = "chargeamt") Long chargeAmt
            ) throws Exception {
        if(chargeAmt <0) {
            throw new CustomException(ErrorCode.CHARGE_INPUT_ERROR);
        }

        if(userLoginId == null) {
            throw new CustomException(ErrorCode.NO_USERINFO);
        }

        long afterAmount = moneyFacade.chargeWithRedisLock(userLoginId,chargeAmt);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("충전 성공")
                .data(afterAmount)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/balance")
    @Operation(summary = "잔액조회", description = "잔액조회")
    public ResponseEntity<CommonResponse<Object>> getBalance(
            @RequestParam(value="username") String userLoginId) throws Exception {

        if(userLoginId==null) {
            throw new CustomException(ErrorCode.NO_USERINFO);
        }

        Long balance = moneyService.getBalance(userLoginId);

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("잔액 조회 성공")
                .data(balance)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
