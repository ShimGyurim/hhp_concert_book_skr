package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.annotation.Distributor;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.RedisRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    RedisRepository redisRepository;

    @Autowired
    RedissonClient redissonClient;

    private final long PUSH_CNT = 100L;

    //스케줄러 역할 하는 메소드
    @Scheduled(fixedRate = 10000)
    @Distributor
    public void queueRefreshSchedule() throws Exception {
        queueRefresh(ApiNo.PAYMENT);
        queueRefresh(ApiNo.BOOK);
    }
    public void queueRefresh(ApiNo apiNo) {

        // WAIT->ACTIVE 로 변경
        List<String> tokenList = redisRepository.popTokensFromWaitingQueue(apiNo.toString(),PUSH_CNT);
        redisRepository.waitRemoves(apiNo.toString(),tokenList);
        redisRepository.pushTokensToActiveQueue(apiNo.toString(),tokenList);

    }

}
