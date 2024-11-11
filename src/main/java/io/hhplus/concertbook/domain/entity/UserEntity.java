package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="userinfo", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_login_id"}) // 중복 토큰 발생 불가
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(name="user_login_id")
    String userLoginId;

    String password;
}
