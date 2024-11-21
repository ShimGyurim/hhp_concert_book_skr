package io.hhplus.concertbook.infra.KafkaProducer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic,String key, Long outboxId) {
        kafkaTemplate.send(topic,key,outboxId);
    }

}