package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ConcertItemRepository extends JpaRepository<ConcertItemEntity,Long> {
    List<ConcertItemEntity> findByConcertD (String scheduleD);

    @Query("SELECT count(s) FROM SeatEntity s WHERE s.concertItem = :concertItemId")
    int countAllSeats(@Param("concertItemId") Long concertItemId);

    @Query("SELECT count(s) FROM SeatEntity s WHERE s.concertItem = :concertItemId AND isUse = false")
    int countAvailSeats(@Param("concertItemId") Long concertItemId);
}
