package io.hhplus.concertbook.event.Pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concertbook.common.enumerate.MQTopic;
import io.hhplus.concertbook.common.enumerate.MQstatus;
import io.hhplus.concertbook.domain.KafkaProducer.PayProducer;
import io.hhplus.concertbook.domain.entity.OutboxEntity;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.domain.service.TokenService;
import io.hhplus.concertbook.infra.KafkaProducer.PayProducerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class PayEventListener {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TokenService tokenService;
    @Autowired
    PayProducer payProducer;

    @Autowired
    OutboxRepository outboxRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void PaySuccessHandler(PayEvent payEvent) throws InterruptedException {
        tokenService.endProcess(payEvent.getWaitToken()); // 토큰 만료
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void PaySuccessApiHandler(PayEvent payEvent) throws InterruptedException {
//        sendPayInfo("id "+payEvent.getPay().getPaymentId()+" 건 결제성공"); // mock api 에 정보 전달
        payProducer.send(MQTopic.PAY_SAVE.toString(),payEvent.getMessageQueueKey(),payEvent.getOutboxId());
    }

//    public void sendPayInfo(String message) throws InterruptedException {
//        log.info("sendPayInfo: "+message);
//        Thread.sleep(2000);
//    }

}
