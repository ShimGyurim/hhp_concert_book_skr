package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentEntity,Long> {
}
