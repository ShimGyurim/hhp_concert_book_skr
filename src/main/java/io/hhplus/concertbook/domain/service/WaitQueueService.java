package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.domain.constant.WaitQueueConstant;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.RedisQueue;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class WaitQueueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RedisQueue redisQueue;

    @Autowired
    RedissonClient redissonClient;

    private final long PUSH_CNT = 100L;

    //스케줄러 역할 하는 메소드 (미작동)
    @Scheduled(fixedRate = 10000)
    public void queueRefreshSchedule() throws Exception {
        final RLock lock = redissonClient.getLock("scheduler");
        boolean available = lock.tryLock(10,2, TimeUnit.SECONDS);

        if(!available) {
            throw new Exception("락 잠금상태");
        }
        queueRefresh(ApiNo.PAYMENT);
        queueRefresh(ApiNo.BOOK);

        lock.unlock();

    }
    public void queueRefresh(ApiNo apiNo) {

        // WAIT->ACTIVE 로 변경
        List<String> tokenList = redisQueue.popTokensFromWaitingQueue(apiNo.toString(),PUSH_CNT);
        redisQueue.waitRemoves(apiNo.toString(),tokenList);
        redisQueue.pushTokensToActiveQueue(apiNo.toString(),tokenList);

    }

}
