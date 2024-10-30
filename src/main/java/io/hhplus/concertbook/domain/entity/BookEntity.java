package io.hhplus.concertbook.domain.entity;

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
    SeatEntity seat;

    @Enumerated(EnumType.STRING)
    BookStatus statusCd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    Timestamp createdAt;
    Timestamp updatedAt;

    @Column(nullable = false)
    @Version
    int version;
}
