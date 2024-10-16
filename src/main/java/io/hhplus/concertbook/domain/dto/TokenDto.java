package io.hhplus.concertbook.domain.dto;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import lombok.Data;

@Data
public class TokenDto {
    String token;
    String userName;
    ApiNo apiNo;
    int waitNo; //TODO: 대기번호 부여
}
