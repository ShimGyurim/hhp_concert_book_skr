package io.hhplus.concertbook.presentation.HttpDto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeatReqDto {
    String token;
//    String dateyymmdd;
//    String timehhmm;
//    String concertName;
    Long concertScheduleId;
}
