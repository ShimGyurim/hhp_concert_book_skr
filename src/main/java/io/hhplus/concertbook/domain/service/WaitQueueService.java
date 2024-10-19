package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.domain.constant.QueueConstant;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class WaitQueueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    BookRepository bookRepository;

    //스케줄러 역할 하는 메소드 (미작동)
    @Transactional
    @Scheduled(fixedRate = 10000)
    public void queueRefreshSchedule() {
        queueRefresh(ApiNo.PAYMENT);
        queueRefresh(ApiNo.BOOK);
    }
    @Transactional
    public void queueRefresh(ApiNo apiNo) {

        // 5분 지난 PROCESS 는 모두 EXPIRED 처리하기
        Timestamp now = Timestamp.from(Instant.now());
        // 5분 전 시간 타임스탬프
        Timestamp fiveMinutesAgo = Timestamp.from(now.toInstant().minus(Duration.ofMinutes(QueueConstant.WAIT_MINUTE)));

        //진행중 토큰이 있는지 확인
        Long count = waitTokenRepository.countStatusToken(apiNo,WaitStatus.PROCESS);


        //5분이상 된 토큰 만료처리
        // PROCESS 없으면 WAIT 중에서 가장 오래된 updatedAt 을 PROCESS 로 처리하기
        waitTokenRepository.updateExpiredTokens(WaitStatus.EXPIRED ,now,fiveMinutesAgo);

        //5분이상 미결제시 만료
        bookRepository.updateExpiredBooks(BookStatus.EXPIRED,now,fiveMinutesAgo,BookStatus.PREPAYMENT);

        if(count > 0L) return; //진행 프로세스가 있으므로 리턴


        //새 process 될 토큰 검색

        List<Long> tokenIds = waitTokenRepository.findFirstTokenIds(WaitStatus.WAIT, apiNo);


        if (!tokenIds.isEmpty()) {
            Long firstTokenId = tokenIds.get(0);
            // 해당 레코드의 status_cd를 "PROCESS"로 업데이트
            waitTokenRepository.updateTokenStatus(WaitStatus.PROCESS, firstTokenId);
            waitTokenRepository.updateTokenUpdatedAt(now,firstTokenId); //새 유효시간 부여
        }
    }

}
