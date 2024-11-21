package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.domain.KafkaProducer.TestProducer;
import io.hhplus.concertbook.infra.KafkaProducer.TestProducerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestKafkaService {
    @Autowired
    private TestProducer testProducer;

//    @GetMapping("/kafkaTest")
    public void kafkaTest() {
        testProducer.create();
    }
}
