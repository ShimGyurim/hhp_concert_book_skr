package io.hhplus.concertbook.event.Book;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concertbook.infra.KafkaProducer.BookProducer;
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
        bookProducer.send("BOOK_SAVE",bookEvent.getMessageQueueKey(),bookEvent.getOutboxId());
    }

    @Scheduled(fixedRate = 20000) // 5분후 발행 체크
    public void BookPubSchedule() throws JsonProcessingException {
        List<OutboxEntity> outboxEntityList = outboxRepository.findAllByTopicAndStatus("BOOK_SAVE","INIT");

        for (OutboxEntity outbox : outboxEntityList) {
            if(outbox.getCreatedAt() == null) continue;
            LocalDateTime someMinAgo = LocalDateTime.now().minus(10, ChronoUnit.SECONDS);
            if(outbox.getCreatedAt().toLocalDateTime().isBefore(someMinAgo)) {
                bookProducer.send("BOOK_SAVE",outbox.getMqKey(),outbox.getOutboxId());
            }
        }

    }



}
