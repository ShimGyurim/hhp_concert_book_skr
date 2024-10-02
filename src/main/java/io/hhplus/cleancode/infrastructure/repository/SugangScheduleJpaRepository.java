package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.infrastructure.entity.SugangScheduleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SugangScheduleJpaRepository extends JpaRepository<SugangScheduleEntity, Long> {

    Optional<SugangScheduleEntity> findBySugang_SugangIdAndClassDate(Long sugang, String classDate);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends SugangScheduleEntity> S save(S entity);

    List<SugangScheduleEntity> findAllByClassDate(String classDate);

}
