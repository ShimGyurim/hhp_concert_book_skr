package io.hhplus.concertbook.domain.dto;

import lombok.Data;

@Data
public class RechargeDto {
    String userLoginId;
    String token;
    int chargeAmt;
}
