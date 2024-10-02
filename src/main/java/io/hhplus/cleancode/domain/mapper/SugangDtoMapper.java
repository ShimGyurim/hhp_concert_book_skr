package io.hhplus.cleancode.domain.mapper;

import io.hhplus.cleancode.domain.dto.SugangDto;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangScheduleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SugangDtoMapper {

    @Autowired
    ModelMapper modelMapper;

    public static List<SugangScheduleEntity> toSugangSchedulesMapper(List<SugangDto> dtos) {
        return dtos.stream()
                .map(dto -> {
                    SugangScheduleEntity schedule = new SugangScheduleEntity();

                    schedule.setClassDate(dto.getClassDate());
                    schedule.setAvailNum(dto.getAvailNum());
                    schedule.setSugang(new SugangEntity(dto.getSugangId()));
//                    schedule.setStudent(new Student(dto.getStudentId()));
                    return schedule;
                })
                .collect(Collectors.toList());
    }



    public static List<SugangDto> toSugangDtoMapper(List<SugangScheduleEntity> entities) {
        return entities.stream()
                .map(entity -> {
                    SugangDto SugangDto = new SugangDto();

                    SugangDto.setClassDate(entity.getClassDate());
                    SugangDto.setSugangId(entity.getSugang().getSugangId());
//                    SugangDto.setStudentId(entity.getStudent().getStudentId());
                    return SugangDto;
                })
                .collect(Collectors.toList());
    }
    
    public static List<SugangDto> historyToSugangDtoMapper(List<SugangHistoryEntity> entities) {
        return entities.stream()
                .map(entity -> {
                    SugangDto SugangDto = new SugangDto();

                    SugangDto.setClassDate(entity.getClassDate());
                    SugangDto.setSugangId(entity.getSugang().getSugangId());
                    SugangDto.setStudentId(entity.getStudent().getStudentId());
                    return SugangDto;
                })
                .collect(Collectors.toList());
    }    
}
