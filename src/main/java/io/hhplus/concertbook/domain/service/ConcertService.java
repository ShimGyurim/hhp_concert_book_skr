package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import io.hhplus.concertbook.domain.repository.ConcertItemRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ConcertService {

    @Autowired
    ConcertItemRepo concertItemRepo;

    public ConcertScheduleDto getAvailSchedule(String scheduleDate){
        List<ConcertItemEntity> concertItems = concertItemRepo.findByConcertD(scheduleDate);

        List<Long> concertSchdules = concertItems.stream()
                .map(item -> {
                    Long id = item.getConcertItemId();

                    return id;
                })
                .collect(Collectors.toList());

        ConcertScheduleDto concertScheduleDto = new ConcertScheduleDto();
        concertScheduleDto.setConcertItemId(concertSchdules);
        return concertScheduleDto;
    }
}
