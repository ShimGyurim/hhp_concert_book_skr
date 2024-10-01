package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.infrastructure.entity.Sugang;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface SugangRepository extends JpaRepository<Sugang, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Sugang> S save(S entity);
}