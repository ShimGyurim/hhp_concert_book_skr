package io.hhplus.concertbook.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concertbook.common.enumerate.MQstatus;
import io.hhplus.concertbook.domain.entity.LogEntity;
import io.hhplus.concertbook.domain.entity.OutboxEntity;
import io.hhplus.concertbook.domain.repository.LogRepository;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.event.Book.BookEvent;
import io.hhplus.concertbook.event.Pay.PayEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
public class PayConsumer {

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    LogRepository logRepository;

    @KafkaListener(topics = "PAY_SAVE", groupId = "group_1")
    public void listen(long outboxId) throws JsonProcessingException, InterruptedException {
        OutboxEntity outboxEntity = outboxRepository.findById(outboxId).get();
        outboxEntity.setStatus(MQstatus.PUBLISHED.toString());
        outboxRepository.save(outboxEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        PayEvent payEvent = objectMapper.readValue(outboxEntity.getPayLoad(), PayEvent.class);
        savePayInfo("id "+payEvent.getPay().getPaymentId()+" 건 결제성공: "+payEvent.getPay().toString());
        outboxEntity.setStatus(MQstatus.COMPLETE.toString());
        outboxRepository.save(outboxEntity);
    }

    //예약정보 저장
    public void savePayInfo(String message) throws InterruptedException {
        log.info("sendPayInfo: "+message);
        LogEntity logEntity = new LogEntity();
        logEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        logEntity.setType("PAY");
        logEntity.setLogMessage(message);
        logRepository.save(logEntity);
    }
}