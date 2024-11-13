package io.hhplus.concertbook.event.Book;

import io.hhplus.concertbook.domain.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class BookEventListener {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TokenService tokenService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void bookSuccessHandler(BookEvent bookEvent) throws InterruptedException {
        tokenService.endProcess(bookEvent.getWaitToken()); // 토큰 만료
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void bookSuccessApiHandler(BookEvent bookEvent) throws InterruptedException {
        sendBookInfo(bookEvent.getBook().getBookId()+"건 예약성공"); // mock api 에 정보 전달
    }

    public void sendBookInfo(String message) throws InterruptedException {
        log.info("sendBookInfo: "+message);
        Thread.sleep(2000);
    }
}
