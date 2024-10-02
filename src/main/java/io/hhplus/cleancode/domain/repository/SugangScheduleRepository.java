package io.hhplus.cleancode.domain.repository;

import io.hhplus.cleancode.domain.entity.SugangSchedule;
import io.hhplus.cleancode.infrastructure.entity.SugangScheduleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SugangScheduleRepository  {

    Optional<SugangSchedule> findBySugang_SugangIdAndClassDate(Long sugang, String classDate);

    <S extends SugangSchedule> S save(S entity);

    List<SugangSchedule> findAllByClassDate(String classDate);

}
