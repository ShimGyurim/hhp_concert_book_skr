package io.hhplus.concertbook;

import io.hhplus.concertbook.common.exception.AmtMinusException;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.presentation.HttpDto.request.BalanceReqDto;
import io.hhplus.concertbook.presentation.HttpDto.request.RechargeReqDto;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.controller.MoneyController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)

public class MoneyUnitTests {

    @InjectMocks
    private MoneyController moneyController;

    @Test
    @DisplayName("잔액 충전 성공 테스트")
    public void rechargeBalance_success() throws Exception {
        // given
        RechargeReqDto reqDto = new RechargeReqDto(100L, "validToken");

        // when
        ResponseEntity<CommonResponse<Object>> response = moneyController.rechargeBalance(reqDto);

        // then
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @DisplayName("잔액 충전 실패 테스트: 음수 넣으면")
    public void rechargeBalance_fail_negativeAmount() throws Exception {
        // given
        RechargeReqDto reqDto = new RechargeReqDto(-100L, "validToken");

        // when, then
        Assertions.assertThrows(AmtMinusException.class, () -> moneyController.rechargeBalance(reqDto));
    }

    @Test
    @DisplayName("잔액 조회 실패 테스트 : 토큰 없이")
    public void getBalance_fail_nullToken() throws Exception {
        // given
        BalanceReqDto reqDto = new BalanceReqDto(null);

        // when, then
//        Assertions.assertThrows(NoTokenException.class, () -> moneyController.getBalance(reqDto));
    }

}
