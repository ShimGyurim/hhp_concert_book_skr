package io.hhplus.cleancode.domain.repository;

import io.hhplus.cleancode.domain.entity.SugangSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SugangScheduleRepository extends JpaRepository<SugangSchedule, Long> {

    Optional<SugangSchedule> findBySugang_SugangIdAndClassDate(Long sugang, String classDate);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends SugangSchedule> S save(S entity);

    List<SugangSchedule> findAllByClassDate(String classDate);

}
