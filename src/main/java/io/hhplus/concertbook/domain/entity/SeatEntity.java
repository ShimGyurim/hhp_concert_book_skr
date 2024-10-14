package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Data
@Entity
@Table(name="seat")

public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long seatId;

    @ManyToOne
    @JoinColumn(name = "concert_item_id")
    long concertItemId;

    int seatNo;
    boolean useYn;
}
