package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name="wallet")

public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wallet_id")
    long walletId;

    long amount; //결제 금액

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Column(nullable = false)
    @Version
    int version;
}
