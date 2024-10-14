package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Data

public class Seat {

    long seatId;

    long concertItemId;

    int seatNo;
    boolean useYn;
}
