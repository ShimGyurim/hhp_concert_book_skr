package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data

public class Payment {

    long paymentId;

    long bookId;

    Timestamp createdAt;
    Timestamp updatedAt;
}
