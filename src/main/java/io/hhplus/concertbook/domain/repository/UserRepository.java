package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserLoginId(String userLoginId);
}
