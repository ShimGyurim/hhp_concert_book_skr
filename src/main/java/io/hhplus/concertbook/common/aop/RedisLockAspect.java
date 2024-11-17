package io.hhplus.concertbook.common.aop;

import io.hhplus.concertbook.common.annotation.Distributor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    RedissonClient redissonClient;

    RLock lock;

    @Around("@annotation(distributor)")
    public Object around(ProceedingJoinPoint joinPoint,Distributor distributor) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String userLoginId = (String) (args.length == 0 ? null : args[0]);

        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        log.info("method name "+methodName+" ditributed aop call");
        // Redis 락 획득 로직
        lock = redissonClient.getLock(userLoginId != null ? userLoginId : methodName);
        boolean available = lock.tryLock(10,2, TimeUnit.SECONDS);

        if(!available) {
            throw new Exception("락 잠금상태");
        }

        try {
            return joinPoint.proceed();
        } finally {
            // Redis 락 해제 로직
            releaseLock();
        }
    }

    private boolean acquireLock() {

        // 락 획득 로직 구현
        return true;
    }

    private void releaseLock() {
        // 락 해제 로직 구현
        log.info("분산락 AOP 락 해제");
        lock.unlock();
    }
}

