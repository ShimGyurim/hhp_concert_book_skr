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
public class RedisQueue {
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> waitQueue;
    private final SetOperations<String, String> activeQueue;

    private final String WAIT_QUEUE = ":WAIT_QUEUE";
    private final String ACTIVE_QUEUE = ":ACTIVE_QUEUE";

    private final long EXPIRE_TIME = 60 * 10L;
    @Autowired
    public RedisQueue(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.waitQueue = redisTemplate.opsForZSet();
        this.activeQueue = redisTemplate.opsForSet();
    }

    public void waitEnqueue(String queueName, String tokenValue) {
        waitEnqueue(queueName, tokenValue, System.currentTimeMillis());
    }
    public void waitEnqueue(String queueName, String tokenValue, double score) {
        waitQueue.add(queueName+WAIT_QUEUE, tokenValue, score);
    }

    public void activeEnqueue(String queueName, String tokenValue) {
        activeQueue.add(queueName+ACTIVE_QUEUE, tokenValue);
    }

    public boolean isValueInWaitQueue (String queueName, String tokenValue) {
        Long rank = waitQueue.rank(queueName+WAIT_QUEUE,tokenValue);
        return rank != null;
    }

    public boolean isValueInActiveQueue (String queueName, String tokenValue) {
        return activeQueue.isMember(queueName+ACTIVE_QUEUE,tokenValue);
    }

    public long getWaitQueueRank (String queueName, String tokenValue) {
        return waitQueue.rank(queueName+WAIT_QUEUE,tokenValue);
    }

    public String waitDequeue(String queueName) {
        Set<String> items = waitQueue.range(queueName, 0, 0);
        if (items != null && !items.isEmpty()) {
            String item = items.iterator().next();
            waitQueue.remove(queueName+WAIT_QUEUE, item);
            return item;
        }
        return null;
    }

    public void waitRemove(String queueName, String tokenValue) {
        waitQueue.remove(queueName+WAIT_QUEUE,tokenValue);
    }

    public void waitRemoves(String queueName, List<String> tokenList) {
        for(String token : tokenList) {
            waitRemove(queueName+WAIT_QUEUE,token);
        }
    }

    public void activeRemove(String queueName, String tokenValue) {
        activeQueue.remove(queueName+ACTIVE_QUEUE,tokenValue);
    }

    public List<String> popTokensFromWaitingQueue(String queueName, long pushCnt) {

        List<String> tokenList = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<String>> tokenSet = waitQueue.popMin(queueName+WAIT_QUEUE, pushCnt);

        for(ZSetOperations.TypedTuple<String> token : tokenSet) {
            tokenList.add(token.getValue().toString());

        }
        return tokenList;
    }


    public List<String> pushTokensToActiveQueue(String queueName, List<String> tokenList) {

        for(String token : tokenList) {

            activeQueue.add(queueName+ACTIVE_QUEUE, token);

        }
        redisTemplate.expire(queueName+ACTIVE_QUEUE, EXPIRE_TIME, TimeUnit.SECONDS);// TTL을 10분(600초)로 설정
        return tokenList;
    }

    public long waitQueueSize(String queueName) {
        return waitQueue.zCard(queueName+WAIT_QUEUE);
    }

    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
