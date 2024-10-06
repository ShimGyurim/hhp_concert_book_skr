package io.hhplus.concertbook.domain.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.concertbook.domain.entity.SugangHistory;

import java.util.List;

public interface SugangHistoryRepository  {

    <S extends SugangHistory> S save(S pojo);

    List<SugangHistory> findAllByStudent_StudentId(Long studentId);
}
