package io.hhplus.concertbook.common.config;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.cache.CacheManager;
import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // LettuceConnectionFactory를 사용하여 Redis 연결 설정 TODO: deprecated 된것 교체
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName(redisHost); // Redis 서버 호스트 이름
        factory.setPort(redisPort); // Redis 서버 포트 번호
//        factory.setPassword("yourpassword"); // Redis 서버 비밀번호 (필요 시 설정)
//        factory.setDatabase(0); // 사용할 Redis 데이터베이스 인덱스
        return factory;
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // key와 value 모두 String으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheManager concertCacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
                .entryTtl(Duration.ofDays(1L)); // 캐시 수명 설정

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf).cacheDefaults(redisCacheConfiguration).build();
    }

}
