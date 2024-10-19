package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;


public interface BookRepository extends JpaRepository<BookEntity,Long> {

    @Modifying
    @Query("UPDATE BookEntity w " +
            "SET w.statusCd = :expireStatus, w.updatedAt = :nowTime " +
            "WHERE w.updatedAt <= :fiveMinutesAgo AND w.statusCd <> :prePayStatus ")
    void updateExpiredBooks(@Param("expireStatus") BookStatus expireStatus, @Param("nowTime") Timestamp nowTime, @Param("fiveMinutesAgo") Timestamp fiveMinutesAgo, @Param("prePayStatus") BookStatus prePayStatus);
}
