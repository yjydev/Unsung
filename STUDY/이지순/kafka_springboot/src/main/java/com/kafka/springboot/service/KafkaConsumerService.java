package com.kafka.springboot.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "first", groupId = "test")
    public void consume(String message) {
        System.out.printf("Consumed message : %s%n", message);
    }
}
