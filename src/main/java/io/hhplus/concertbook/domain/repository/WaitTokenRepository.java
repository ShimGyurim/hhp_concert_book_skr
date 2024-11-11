package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WaitTokenRepository extends JpaRepository<WaitTokenEntity,Long> {

    WaitTokenEntity findByUser_UserLoginIdAndServiceCd(String userLoginId, ApiNo apiNo);
    WaitTokenEntity findByToken(String token);

    @Query("SELECT w.user FROM WaitTokenEntity w  WHERE w.token = :token")
    UserEntity findUserinfoByToken(@Param("token") String token);

}
