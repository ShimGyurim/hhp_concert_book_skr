package io.hhplus.concertbook.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="concertItem")

public class ConcertItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_item_id")
    long concertItemId;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    long concertId;

    String concertD;
    String concertT;
    int allseats;
    int availSeats;

}
