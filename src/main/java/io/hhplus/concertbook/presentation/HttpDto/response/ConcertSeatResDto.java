package io.hhplus.concertbook.presentation.HttpDto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeatResDto {
    int seatsNo;
    Long seatId;
    Long concertScheduleId;
}
