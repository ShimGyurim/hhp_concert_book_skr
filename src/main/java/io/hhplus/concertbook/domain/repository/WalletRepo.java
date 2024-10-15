package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepo extends JpaRepository<WalletEntity,Long> {
    WalletEntity findByUser_UserId(long userId);
}
