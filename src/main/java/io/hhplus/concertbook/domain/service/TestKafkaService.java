package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.infra.KafkaProducer.TestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class TestKafkaService {
    @Autowired
    private TestProducer testProducer;

//    @GetMapping("/kafkaTest")
    public void kafkaTest() {
        testProducer.create();
    }
}
