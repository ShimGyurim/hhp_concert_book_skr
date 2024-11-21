package io.hhplus.concertbook.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concertbook.domain.entity.LogEntity;
import io.hhplus.concertbook.domain.entity.OutboxEntity;
import io.hhplus.concertbook.domain.repository.LogRepository;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.event.Book.BookEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
public class BookConsumer {

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    LogRepository logRepository;

    @KafkaListener(topics = "BOOK_SAVE", groupId = "group_1")
    public void listen(long outboxId) throws JsonProcessingException, InterruptedException {
        OutboxEntity outboxEntity = outboxRepository.findById(outboxId).get();
        outboxEntity.setStatus("PUBLISHED");
        outboxRepository.save(outboxEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        BookEvent bookEvent = objectMapper.readValue(outboxEntity.getPayLoad(), BookEvent.class);
        sendBookInfo("id "+bookEvent.getBook().getBookId()+" 건 예약성공");
        outboxEntity.setStatus("COMPLETE");
        outboxRepository.save(outboxEntity);
    }

    //예약정보 저장
    public void sendBookInfo(String message) throws InterruptedException {
        log.info("sendBookInfo: "+message);
        LogEntity logEntity = new LogEntity();
        logEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        logEntity.setType("BOOK");
        logEntity.setLogMessage(message);
        logRepository.save(logEntity);
    }
}