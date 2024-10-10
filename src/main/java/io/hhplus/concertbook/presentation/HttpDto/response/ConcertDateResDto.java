package io.hhplus.concertbook.presentation.HttpDto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDateResDto {
    String dateyymmdd;
    String timemmdd;
    Long concertScheduleId;
    String concertName;
    int availSeats;
}
