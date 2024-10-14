package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    long userId;
}
