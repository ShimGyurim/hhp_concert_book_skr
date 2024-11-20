package io.hhplus.concertbook.event.Book;

import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEvent {
    BookEntity book;
    WaitTokenEntity waitToken;
    long outboxId;
    String messageQueueKey;
}
