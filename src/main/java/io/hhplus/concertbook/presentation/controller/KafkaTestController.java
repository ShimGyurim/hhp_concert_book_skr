package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.domain.service.TestKafkaService;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class KafkaTestController {

    @Autowired
    TestKafkaService testKafkaService;

    @GetMapping("/kafkaTest")
    public ResponseEntity<CommonResponse<Object>> kafkaTest() {
        testKafkaService.kafkaTest();

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
