package io.hhplus.concertbook.domain.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data

public class Concert {

    private long concertId;

    private String concertName;
    private long fee;


}