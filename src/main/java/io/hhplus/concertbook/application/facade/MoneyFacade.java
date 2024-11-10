package io.hhplus.concertbook.application.facade;

import io.hhplus.concertbook.common.annotation.Distributor;
import io.hhplus.concertbook.domain.service.MoneyService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MoneyFacade {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MoneyService moneyService;

    @Distributor
    public long chargeWithRedisLock(String userLoginId,Long chargeAmt) throws Exception {
//        final RLock lock = redissonClient.getLock(userLoginId);
//        boolean available = lock.tryLock(10,2, TimeUnit.SECONDS);

//        if(!available) {
//            throw new Exception("락 잠금상태");
//        }
        Long chargeResult = moneyService.charge(userLoginId,chargeAmt);
//        lock.unlock();
        return chargeResult;
    }
}
