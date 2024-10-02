package io.hhplus.cleancode.domain.repository;

import io.hhplus.cleancode.domain.entity.Student;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Student> S save(S entity);
}
