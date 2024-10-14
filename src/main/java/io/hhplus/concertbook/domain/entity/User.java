package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data

public class User {

    long userId;

    String userName;
}
