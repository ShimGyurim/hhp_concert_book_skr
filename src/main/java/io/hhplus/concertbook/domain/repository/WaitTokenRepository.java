package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


public interface WaitTokenRepository extends JpaRepository<WaitTokenEntity,Long> {

    WaitTokenEntity findByUser_UserNameAndServiceCd(String userName, ApiNo apiNo);
    WaitTokenEntity findByToken(String token);

    @Query("SELECT w.user FROM WaitTokenEntity w  WHERE w.token = :token")
    UserEntity findUserinfoByToken(@Param("token") String token);

    @Query("SELECT COUNT(w) FROM WaitTokenEntity w WHERE w.serviceCd = :serviceCd AND w.statusCd in ('WAIT','PROCESS') AND w.updatedAt < :updatedAtThis")
    Long countPreviousToken(@Param("serviceCd") ApiNo serviceCd,  @Param("updatedAtThis") Timestamp updatedAtThis);

    @Query("SELECT COUNT(w) FROM WaitTokenEntity w WHERE w.serviceCd = :serviceCd AND w.statusCd = :statusCd ")
    Long countStatusToken(@Param("serviceCd") ApiNo serviceCd, @Param("statusCd") WaitStatus statusCd);

    @Modifying
    @Query("UPDATE WaitTokenEntity w " +
            "SET w.statusCd = :expireStatus, w.expiredAt = :nowTime " +
            "WHERE w.updatedAt <= :fiveMinutesAgo AND w.statusCd <> 'END' ")
    void updateExpiredTokens(@Param("expireStatus") WaitStatus expireStatus,  @Param("nowTime") Timestamp nowTime, @Param("fiveMinutesAgo") Timestamp fiveMinutesAgo);

    @Query("SELECT w.tokenId FROM WaitTokenEntity w WHERE w.statusCd = :waitStatus AND w.serviceCd = :apiNo ORDER BY w.updatedAt ASC")
    List<Long> findFirstTokenIds(@Param("waitStatus") WaitStatus waitStatus, @Param("apiNo") ApiNo apiNo);

    @Modifying
    @Query("UPDATE WaitTokenEntity w SET w.statusCd = :processStatus WHERE w.tokenId = :tokenId")
    void updateTokenStatus(@Param("processStatus") WaitStatus processStatus, @Param("tokenId") Long tokenId);

    @Modifying
    @Query("UPDATE WaitTokenEntity w SET w.updatedAt = :nowTime WHERE w.tokenId = :tokenId")
    void updateTokenUpdatedAt(@Param("nowTime") Timestamp nowTime, @Param("tokenId") Long tokenId);

}
