package io.hhplus.concertbook.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concertbook.common.enumerate.MQTopic;
import io.hhplus.concertbook.common.enumerate.MQstatus;
import io.hhplus.concertbook.domain.KafkaProducer.BookProducer;
import io.hhplus.concertbook.domain.KafkaProducer.PayProducer;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.event.Book.BookEvent;
import io.hhplus.concertbook.event.Pay.PayEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class KafkaSchedulerService {
    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    BookProducer bookProducer;

    @Autowired
    PayProducer payProducer;

    @Scheduled(fixedRate = 20000) // 5분후 발행 체크
    public void BookPubSchedule() throws JsonProcessingException {
        List<OutboxEntity> outboxEntityList = outboxRepository.findAllByTopicAndStatus(MQTopic.BOOK_SAVE.toString(), MQstatus.INIT.toString());

        for (OutboxEntity outbox : outboxEntityList) {
            if(outbox.getCreatedAt() == null) continue;
            LocalDateTime someMinAgo = LocalDateTime.now().minus(10, ChronoUnit.SECONDS);
            if(outbox.getCreatedAt().toLocalDateTime().isBefore(someMinAgo)) {
                bookProducer.send(MQTopic.BOOK_SAVE.toString(),outbox.getMqKey(),outbox.getOutboxId());
            }
        }

    }

    @Scheduled(fixedRate = 20000) // 5분후 발행 체크
    public void PayPubSchedule() throws JsonProcessingException {
        List<OutboxEntity> outboxEntityList = outboxRepository.findAllByTopicAndStatus(MQTopic.PAY_SAVE.toString(), MQstatus.INIT.toString());

        for (OutboxEntity outbox : outboxEntityList) {
            if(outbox.getCreatedAt() == null) continue;
            LocalDateTime someMinAgo = LocalDateTime.now().minus(10, ChronoUnit.SECONDS);
            if(outbox.getCreatedAt().toLocalDateTime().isBefore(someMinAgo)) {
                payProducer.send(MQTopic.PAY_SAVE.toString(),outbox.getMqKey(),outbox.getOutboxId());
            }
        }

    }


}
