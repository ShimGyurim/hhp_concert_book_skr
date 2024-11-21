package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="log")
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    long logId;

    @Lob
    String logMessage;
    String type;

    Timestamp createdAt;
}


