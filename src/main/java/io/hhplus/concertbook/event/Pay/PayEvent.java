package io.hhplus.concertbook.event.Pay;

import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.PaymentEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class PayEvent {
    PaymentEntity pay;
    WaitTokenEntity waitToken;
}
