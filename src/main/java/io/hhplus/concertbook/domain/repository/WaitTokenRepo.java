package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitTokenRepo extends JpaRepository<WaitTokenEntity,Long> {

    WaitTokenEntity findByUserNameAndServiceCd(String userName, ApiNo apiNo);
}
