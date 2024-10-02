package io.hhplus.cleancode.domain.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.cleancode.domain.entity.SugangHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SugangHistoryRepository extends JpaRepository<SugangHistory, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends SugangHistory> S save(S entity);

//    @Override
//    Optional<SugangHistory> findById(Long aLong);

//    @Override
//    List<SugangHistory> findAllById(Iterable<Long> longs);

    List<SugangHistory> findAllByStudent_StudentId(Long studentId);
}
