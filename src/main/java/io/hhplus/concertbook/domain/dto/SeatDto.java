package io.hhplus.concertbook.domain.dto;

import lombok.Data;

@Data
public class SeatDto {
    Long seatId;
    int seatNo;
    boolean isUse;
}
