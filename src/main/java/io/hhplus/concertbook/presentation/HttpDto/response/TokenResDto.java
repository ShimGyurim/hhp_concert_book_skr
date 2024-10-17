package io.hhplus.concertbook.presentation.HttpDto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResDto {
    int curWaitNo;
    String token;
    String userId;
}
