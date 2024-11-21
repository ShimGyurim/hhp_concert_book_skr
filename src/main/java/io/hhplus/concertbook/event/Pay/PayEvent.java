package io.hhplus.concertbook.event.Pay;

import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.PaymentEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayEvent {
    PaymentEntity pay;
    WaitTokenEntity waitToken;
    long outboxId;
    String messageQueueKey;
}
