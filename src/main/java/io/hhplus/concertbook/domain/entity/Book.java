package io.hhplus.concertbook.domain.entity;

import io.hhplus.concertbook.common.enumerate.BookStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data

public class Book {
    long bookId;

    long seatId;

    BookStatus statusCd;

    long userId;

    Timestamp createdAt;
    Timestamp updatedAt;

}
