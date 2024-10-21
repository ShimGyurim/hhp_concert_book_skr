package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface WalletRepository extends JpaRepository<WalletEntity,Long> {
    WalletEntity findByUser_UserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w JOIN w.UserEntity u WHERE u.user_id = :userId")
    WalletEntity findByUser_UserIdWithLock(@Param("userId") long userId);

}
