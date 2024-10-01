package io.hhplus.cleancode.infrastructure.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.cleancode.infrastructure.entity.SugangHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface SugangHistoryRepository extends JpaRepository<SugangHistory, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends SugangHistory> S save(S entity);
}
