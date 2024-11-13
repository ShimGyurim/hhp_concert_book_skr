package io.hhplus.concertbook.event.Pay;

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
public class PayEventListener {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TokenService tokenService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void PaySuccessHandler(PayEvent payEvent) throws InterruptedException {
        tokenService.endProcess(payEvent.getWaitToken()); // 토큰 만료
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void PaySuccessApiHandler(PayEvent payEvent) throws InterruptedException {
        sendPayInfo(payEvent.getPay().getPaymentId()+"건 결제성공"); // mock api 에 정보 전달
    }

    public void sendPayInfo(String message) throws InterruptedException {
        log.info("sendPayInfo: "+message);
        Thread.sleep(2000);
    }
}
