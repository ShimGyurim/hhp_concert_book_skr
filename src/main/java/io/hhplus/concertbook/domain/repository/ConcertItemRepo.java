package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertItemRepo extends JpaRepository<ConcertItemEntity,Long> {
}
