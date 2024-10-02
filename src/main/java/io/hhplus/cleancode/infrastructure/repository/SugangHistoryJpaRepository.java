package io.hhplus.cleancode.infrastructure.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SugangHistoryJpaRepository extends JpaRepository<SugangHistoryEntity, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends SugangHistoryEntity> S save(S entity);

//    @Override
//    Optional<SugangHistory> findById(Long aLong);

//    @Override
//    List<SugangHistory> findAllById(Iterable<Long> longs);

    List<SugangHistoryEntity> findAllByStudent_StudentId(Long studentId);
}
