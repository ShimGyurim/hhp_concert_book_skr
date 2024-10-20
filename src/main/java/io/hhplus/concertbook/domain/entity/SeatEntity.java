package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name="seat")
@Builder
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    long seatId;

    @ManyToOne
    @JoinColumn(name = "concert_item_id")
    ConcertItemEntity concertItem;

    int seatNo;
    boolean isUse;
}
