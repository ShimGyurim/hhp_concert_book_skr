package io.hhplus.concertbook.infra.KafkaProducer;

import io.hhplus.concertbook.domain.KafkaProducer.TestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestProducerImpl implements TestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void create() {
        kafkaTemplate.send("topic", 1234567890L);
    }

}