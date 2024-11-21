package io.hhplus.concertbook.domain.KafkaProducer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

public interface BookProducer {

    public void send(String topic,String key, Long outboxId);

}