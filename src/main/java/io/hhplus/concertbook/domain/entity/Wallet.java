package io.hhplus.concertbook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data

public class Wallet {

    long walletId;

    long amount; //결제 금액

    long userId;
}
