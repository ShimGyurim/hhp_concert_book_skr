package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="outbox")
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id")
    long outboxId;
    String mqKey;

    String status;
    String topic; // book

    @Lob
    String payLoad;
    String type;

    Timestamp createdAt;
    Timestamp updatedAt;
}


