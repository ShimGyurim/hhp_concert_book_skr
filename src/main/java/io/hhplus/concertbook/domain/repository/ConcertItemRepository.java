package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ConcertItemRepository extends JpaRepository<ConcertItemEntity,Long> {
    List<ConcertItemEntity> findByConcertD (String scheduleD);
}
