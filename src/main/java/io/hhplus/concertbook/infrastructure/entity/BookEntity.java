package io.hhplus.concertbook.infrastructure.entity;

import io.hhplus.concertbook.common.enumerate.BookStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="book")

public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    long bookId;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    long seatId;

    @Enumerated(EnumType.STRING)
    BookStatus statusCd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    long userId;

    Timestamp createdAt;
    Timestamp updatedAt;

}
