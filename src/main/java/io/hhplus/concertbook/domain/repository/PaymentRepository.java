package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
    long countByBook_BookId(long bookId);
}
