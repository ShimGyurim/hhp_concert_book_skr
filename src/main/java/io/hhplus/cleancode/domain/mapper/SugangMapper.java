package io.hhplus.cleancode.domain.mapper;

import io.hhplus.cleancode.domain.DTO.SugangDto;
import io.hhplus.cleancode.infrastructure.entity.Student;
import io.hhplus.cleancode.infrastructure.entity.Sugang;
import io.hhplus.cleancode.infrastructure.entity.SugangHistory;
import io.hhplus.cleancode.infrastructure.entity.SugangSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SugangMapper {

    @Autowired
    ModelMapper modelMapper;

    public static List<SugangSchedule> toSugangSchedulesMapper(List<SugangDto> dtos) {
        return dtos.stream()
                .map(dto -> {
                    SugangSchedule schedule = new SugangSchedule();

                    schedule.setClassDate(dto.getClassDate());
                    schedule.setAvailNum(dto.getAvailNum());
                    schedule.setSugang(new Sugang(dto.getSugangId()));
                    schedule.setStudent(new Student(dto.getStudentId()));
                    return schedule;
                })
                .collect(Collectors.toList());
    }



    public static List<SugangDto> toSugangDtoMapper(List<SugangSchedule> entities) {
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
    
    public static List<SugangDto> historyToSugangDtoMapper(List<SugangHistory> entities) {
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
