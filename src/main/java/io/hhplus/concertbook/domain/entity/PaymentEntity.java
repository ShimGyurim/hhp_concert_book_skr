package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    long paymentId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    long bookId;

    Timestamp createdAt;
    Timestamp updatedAt;
}
