package io.hhplus.cleancode.domain.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.cleancode.domain.entity.SugangHistory;
import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SugangHistoryRepository  {

    <S extends SugangHistory> S save(S pojo);

    List<SugangHistory> findAllByStudent_StudentId(Long studentId);
}
