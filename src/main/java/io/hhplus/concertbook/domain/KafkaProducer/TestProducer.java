package io.hhplus.concertbook.domain.KafkaProducer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

public interface TestProducer {

    public void create();

}