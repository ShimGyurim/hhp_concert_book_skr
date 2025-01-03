package io.hhplus.concertbook.domain.dto;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import lombok.Data;

@Data
public class TokenDto {
    String token;
    String userLoginId;
    ApiNo apiNo;
    long waitNo;
    WaitStatus waitStatus;
}
