package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data

public class ConcertItem {

    long concertItemId;

    long concertId;

    String concertD;
    String concertT;
    int allseats;
    int availSeats;

}
