package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.LogEntity;
import io.hhplus.concertbook.domain.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogRepository extends JpaRepository<LogEntity,Long> {

}
