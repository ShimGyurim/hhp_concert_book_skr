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
    @Column(name = "seat_id")
    long seatId;

    @ManyToOne
    @JoinColumn(name = "concert_item_id")
    ConcertItemEntity concertItem;

    int seatNo;
    boolean useYn;
}
