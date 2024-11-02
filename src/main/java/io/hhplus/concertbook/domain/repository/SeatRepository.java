package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.SeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SeatRepository extends JpaRepository<SeatEntity,Long> {
    List<SeatEntity> findByConcertItem_ConcertItemId(Long aLong);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM SeatEntity s WHERE s.seatId = :seatId")
    SeatEntity findByIdWithLock(@Param("seatId") long seatId);
}
