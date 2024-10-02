package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.infrastructure.entity.StudentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends StudentEntity> S save(S entity);
}
