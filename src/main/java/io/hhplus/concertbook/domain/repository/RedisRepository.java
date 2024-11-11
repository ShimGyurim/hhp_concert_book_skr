package io.hhplus.concertbook.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> waitQueue;
    private final SetOperations<String, String> activeQueue;

    private final String WAIT_QUEUE = "WAIT_QUEUE:";
    private final String ACTIVE_QUEUE = "ACTIVE_QUEUE:";

    private final long EXPIRE_TIME = 60 * 10L;
    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.waitQueue = redisTemplate.opsForZSet();
        this.activeQueue = redisTemplate.opsForSet();
    }

    public void waitEnqueue(String queueName, String tokenValue) {
        waitEnqueue(queueName, tokenValue, System.currentTimeMillis());
    }
    public void waitEnqueue(String queueName, String tokenValue, double score) {
        waitQueue.add(WAIT_QUEUE+queueName, tokenValue, score);
    }

    public void activeEnqueue(String queueName, String tokenValue) {
        activeQueue.add(ACTIVE_QUEUE+queueName, tokenValue);
    }

    public boolean isValueInWaitQueue (String queueName, String tokenValue) {
        Long rank = waitQueue.rank(WAIT_QUEUE+queueName,tokenValue);
        return rank != null;
    }

    public boolean isValueInActiveQueue (String queueName, String tokenValue) {
        return activeQueue.isMember(ACTIVE_QUEUE+queueName,tokenValue);
    }

    public long getWaitQueueRank (String queueName, String tokenValue) {
        return waitQueue.rank(WAIT_QUEUE+queueName,tokenValue);
    }

    public String waitDequeue(String queueName) {
        Set<String> items = waitQueue.range(queueName, 0, 0);
        if (items != null && !items.isEmpty()) {
            String item = items.iterator().next();
            waitQueue.remove(WAIT_QUEUE+queueName, item);
            return item;
        }
        return null;
    }

    public void waitRemove(String queueName, String tokenValue) {
        waitQueue.remove(WAIT_QUEUE+queueName,tokenValue);
    }

    public void waitRemoves(String queueName, List<String> tokenList) {
        for(String token : tokenList) {
            waitRemove(WAIT_QUEUE+queueName,token);
        }
    }

    public void activeRemove(String queueName, String tokenValue) {
        activeQueue.remove(ACTIVE_QUEUE+queueName,tokenValue);
    }

    public List<String> popTokensFromWaitingQueue(String queueName, long pushCnt) {

        List<String> tokenList = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<String>> tokenSet = waitQueue.popMin(WAIT_QUEUE+queueName, pushCnt);

        for(ZSetOperations.TypedTuple<String> token : tokenSet) {
            tokenList.add(token.getValue().toString());

        }
        return tokenList;
    }


    public List<String> pushTokensToActiveQueue(String queueName, List<String> tokenList) {

        for(String token : tokenList) {

            activeQueue.add(ACTIVE_QUEUE+queueName, token);

        }
        redisTemplate.expire(ACTIVE_QUEUE+queueName, EXPIRE_TIME, TimeUnit.SECONDS);// TTL을 10분(600초)로 설정
        return tokenList;
    }

    public long waitQueueSize(String queueName) {
        return waitQueue.zCard(WAIT_QUEUE+queueName);
    }

    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
