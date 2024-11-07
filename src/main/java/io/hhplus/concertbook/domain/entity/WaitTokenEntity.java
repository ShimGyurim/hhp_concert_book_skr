package io.hhplus.concertbook.domain.entity;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="wait_token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"}) // 중복 토큰 발생 불가
})

public class WaitTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    long tokenId;

    Timestamp createdAt;
    Timestamp updatedAt;
    Timestamp expiredAt;

//    @Enumerated(EnumType.STRING)
//    WaitStatus statusCd;

    @Enumerated(EnumType.STRING)
    ApiNo serviceCd;

    String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

//    public void endProcess() {
//        this.setStatusCd(WaitStatus.END);
//    }
}
