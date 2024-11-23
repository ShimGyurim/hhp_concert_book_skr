package io.hhplus.concertbook.event.Book;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concertbook.common.enumerate.MQTopic;
import io.hhplus.concertbook.common.enumerate.MQstatus;
import io.hhplus.concertbook.domain.KafkaProducer.BookProducer;
import io.hhplus.concertbook.domain.entity.OutboxEntity;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.domain.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookEventListener {

    @Autowired
    TokenService tokenService;

    @Autowired
    BookProducer bookProducer;

    @Autowired
    OutboxRepository outboxRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void bookSuccessHandler(BookEvent bookEvent) throws InterruptedException {
        tokenService.endProcess(bookEvent.getWaitToken()); // 토큰 만료
    }

//    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void bookSuccessApiHandler(BookEvent bookEvent) throws InterruptedException {
        bookProducer.send(MQTopic.BOOK_SAVE.toString(),bookEvent.getMessageQueueKey(),bookEvent.getOutboxId());
    }





}
