package io.hhplus.concertbook.infrastructure.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Primary;

@Data
@Entity
@Table(name="concert")

public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_id")
    private long concertId;

    private String concertName;
    private long fee;


}