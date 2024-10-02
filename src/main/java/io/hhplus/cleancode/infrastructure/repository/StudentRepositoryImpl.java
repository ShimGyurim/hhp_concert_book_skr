package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.domain.entity.Student;
import io.hhplus.cleancode.domain.repository.StudentRepository;
import io.hhplus.cleancode.infrastructure.entity.StudentEntity;
import io.hhplus.cleancode.infrastructure.mapper.StudentMapper;

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
