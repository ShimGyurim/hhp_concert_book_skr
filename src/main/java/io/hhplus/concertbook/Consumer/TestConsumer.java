package io.hhplus.concertbook.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

    @KafkaListener(topics = "topic", groupId = "group_1")
    public void listen(long message) {
        System.out.println("Received Messasge in group group_1: " + message);
    }

}