package io.hhplus.concertbook.presentation.HttpDto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayReqDto {
    Long bookId;
    String token;
}
