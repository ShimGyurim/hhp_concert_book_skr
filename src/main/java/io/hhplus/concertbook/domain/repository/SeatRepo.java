package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<SeatEntity,Long> {
    List<SeatEntity> findByConcertItem_ConcertItemId(Long aLong);
}
