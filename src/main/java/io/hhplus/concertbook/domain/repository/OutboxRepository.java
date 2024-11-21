package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.OutboxEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OutboxRepository extends JpaRepository<OutboxEntity,Long> {
    List<OutboxEntity> findAllByTopicAndStatus(String topic,String status);
}
