package io.hhplus.cleancode.domain.repository;

import io.hhplus.cleancode.domain.entity.Sugang;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

public interface SugangRepository  {

    <S extends Sugang> S save(S item);
}
