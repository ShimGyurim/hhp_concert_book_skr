package io.hhplus.concertbook.common.RedisService;

import io.hhplus.concertbook.domain.service.MoneyService;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class LockReleaseListener {
    private final RedissonClient redissonClient;
    private final MoneyService moneyService;

    public LockReleaseListener(RedissonClient redissonClient, MoneyService moneyService) {
        this.redissonClient = redissonClient;
        this.moneyService = moneyService;
    }

    @PostConstruct
    public void subscribeToLockRelease() {
        RTopic topic = redissonClient.getTopic("chargeLockReleased");
        topic.addListener(RedisLockService.LockReleaseChargeMessage.class, new MessageListener<RedisLockService.LockReleaseChargeMessage>() {
            @Override
            public void onMessage(CharSequence channel, RedisLockService.LockReleaseChargeMessage message) {
                try {
                    moneyService.charge(message.getUserName(), message.getChargeAmt());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
