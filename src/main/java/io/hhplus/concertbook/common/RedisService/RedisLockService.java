package io.hhplus.concertbook.common.RedisService;

import lombok.Getter;
import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {
    private final RedissonClient redissonClient;

    public RedisLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void lockAndNotify(String lockName, String userName, long chargeAmt) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockName);
        boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
        if (isLocked) {
            try {
                // 작업 수행
            } finally {
                lock.unlock();
                RTopic topic = redissonClient.getTopic("chargeLockReleased");
                topic.publish(new LockReleaseChargeMessage(lockName, userName, chargeAmt));
            }
        }
    }

    @Getter
    public static class LockReleaseChargeMessage {
        private String lockName;
        private String userName;
        private long chargeAmt;


        public LockReleaseChargeMessage(String lockName, String userName, long chargeAmt) {
            this.lockName = lockName;
            this.userName = userName;
            this.chargeAmt = chargeAmt;

        }

        // Getters and setters
    }
}
