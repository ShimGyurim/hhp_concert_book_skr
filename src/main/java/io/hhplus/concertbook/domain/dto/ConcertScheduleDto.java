package io.hhplus.concertbook.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConcertScheduleDto {
    List<Long> concertItemId;
}
