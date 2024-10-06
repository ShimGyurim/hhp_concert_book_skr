package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.SugangSchedule;

import java.util.List;
import java.util.Optional;

public interface SugangScheduleRepository  {

    Optional<SugangSchedule> findBySugang_SugangIdAndClassDate(Long sugang, String classDate);

    <S extends SugangSchedule> S save(S entity);

    List<SugangSchedule> findAllByClassDate(String classDate);

}
