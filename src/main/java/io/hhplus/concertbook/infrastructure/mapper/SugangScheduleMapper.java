package io.hhplus.concertbook.infrastructure.mapper;

import io.hhplus.concertbook.domain.entity.SugangSchedule;
import io.hhplus.concertbook.infrastructure.entity.SugangScheduleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SugangScheduleMapper {

    @Autowired
    static ModelMapper modelMapper;

    public static SugangScheduleEntity sugangScheduleToEntity (SugangSchedule sugangSchedule) {
        return modelMapper.map(sugangSchedule,SugangScheduleEntity.class);
    }

    public static SugangSchedule sugangScheduleToPojo  (SugangScheduleEntity sugangScheduleEntity) {
        return modelMapper.map(sugangScheduleEntity,SugangSchedule.class);
    }

    public static List<SugangScheduleEntity> sugangScheduleToEntityList (List<SugangSchedule> list) {
        return list.stream()
                .map(item -> {
                    return modelMapper.map(item,SugangScheduleEntity.class);
                })
                .collect(Collectors.toList());
    }

    public static List<SugangSchedule> sugangScheduleToPojoList (List<SugangScheduleEntity> list) {
        return list.stream()
                .map(item -> {
                    return modelMapper.map(item,SugangSchedule.class);
                })
                .collect(Collectors.toList());
    }    
}
