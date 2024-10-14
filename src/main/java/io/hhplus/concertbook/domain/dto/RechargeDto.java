package io.hhplus.concertbook.domain.dto;

import lombok.Data;

@Data
public class RechargeDto {
    String userName;
    String token;
    int chargeAmt;
}
