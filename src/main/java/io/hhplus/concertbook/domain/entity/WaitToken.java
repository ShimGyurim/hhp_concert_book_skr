package io.hhplus.concertbook.domain.entity;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data

public class WaitToken {

    long tokenId;

    Timestamp createdAt;
    Timestamp updatedAt;
    Timestamp expiredAt;

    WaitStatus statusCd;

    ApiNo serviceCd;

    String token;

    long userId;
}
