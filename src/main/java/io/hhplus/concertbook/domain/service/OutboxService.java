package io.hhplus.concertbook.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.OutboxRepository;
import io.hhplus.concertbook.event.Book.BookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class OutboxService {
    @Autowired
    OutboxRepository outboxRepository;

    public BookEvent bookOutboxService (BookEntity book, SeatEntity seat, WaitTokenEntity waitToken) throws JsonProcessingException {
        OutboxEntity outboxEntity = new OutboxEntity();
        outboxEntity.setMqKey("BOOK:"+seat.getSeatId());
        outboxEntity.setTopic("BOOK_SAVE");
        outboxEntity.setType("BOOK");
        outboxEntity.setStatus("INIT");
        outboxEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        ObjectMapper objectMapper = new ObjectMapper();
        BookEvent bookEvent = new BookEvent(book,waitToken,-1L,outboxEntity.getMqKey());
        OutboxEntity newOutbox = outboxRepository.save(outboxEntity);
        bookEvent.setOutboxId(newOutbox.getOutboxId());
        String payLoad = objectMapper.writeValueAsString(bookEvent);
        newOutbox.setPayLoad(payLoad);
        outboxRepository.save(newOutbox);

        return bookEvent;
    }
}
