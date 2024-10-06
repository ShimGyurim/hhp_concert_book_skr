package io.hhplus.concertbook.infrastructure.repository;

import io.hhplus.concertbook.domain.entity.Student;
import io.hhplus.concertbook.domain.repository.StudentRepository;
import io.hhplus.concertbook.infrastructure.entity.StudentEntity;
import io.hhplus.concertbook.infrastructure.mapper.StudentMapper;

public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository studentJpaRepository;

    public StudentRepositoryImpl(StudentJpaRepository studentJpaRepository) {
        this.studentJpaRepository = studentJpaRepository;
    }

    @Override
    public <S extends Student> S save(S pojo) {
        StudentEntity studentEntity = studentJpaRepository.save(StudentMapper.studentToEntity(pojo));
        return (S) StudentMapper.studentToPojo(studentEntity);
    }
}
