package io.hhplus.concertbook.infrastructure.mapper;

import io.hhplus.concertbook.domain.entity.Student;
import io.hhplus.concertbook.infrastructure.entity.StudentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentMapper {

    @Autowired
    static ModelMapper modelMapper;

    public static StudentEntity  studentToEntity (Student student) {
        return modelMapper.map(student,StudentEntity.class);
    }

    public static Student studentToPojo (StudentEntity studentEntity) {
        return modelMapper.map(studentEntity,Student.class);
    }
}
