package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="userinfo", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_name"}) // 중복 토큰 발생 불가
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(name="user_name")
    String userName;

    @Column(name="password")
    String password;
}
