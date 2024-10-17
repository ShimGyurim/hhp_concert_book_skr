package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<ConcertEntity,Long> {

}
