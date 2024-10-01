package io.hhplus.cleancode.domain.service;

import io.hhplus.cleancode.application.dto.SugangInsertDto;
import io.hhplus.cleancode.application.dto.SugangSelectDto;
import io.hhplus.cleancode.infrastructure.entity.Student;
import io.hhplus.cleancode.infrastructure.entity.Sugang;
import io.hhplus.cleancode.infrastructure.entity.SugangSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SugangMapper {

    @Autowired
    ModelMapper modelMapper;

//    TestDto testDto = modelMapper.map(testEntity, TestDto.class);

//    public SugangSelectDto SugangMapperToDto (SugangSchedule sugangSchedule) {
//        return
//    }
//
//    public SugangSelectDto SugangMapperToDtoList (SugangSchedule sugangSchedule) {
//        return
//    }
    public static List<SugangSchedule> toSugangSchedulesMapper(List<SugangInsertDto> dtos) {
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



    public static List<SugangSelectDto> toSugangSelectDtoMapper(List<SugangSchedule> entities) {
        return entities.stream()
                .map(entity -> {
                    SugangSelectDto sugangSelectDto = new SugangSelectDto();
                    sugangSelectDto.setClassDate(entity.getClassDate());
//                    sugangSelectDto.setAvailNum(entity.getAvailNum());
                    sugangSelectDto.setSugangId(entity.getSugang().getSugangId());
                    sugangSelectDto.setStudentId(entity.getStudent().getStudentId());
                    return sugangSelectDto;
                })
                .collect(Collectors.toList());
    }
}
