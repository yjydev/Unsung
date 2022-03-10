package com.kafka.springboot.controller;

import com.kafka.springboot.service.KafkaProducerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final KafkaProducerService producer;

    @Autowired
    KafkaController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @ApiOperation(value = "send message", notes = "KAFKA test")
    @PostMapping("/")
    public String sendMessage(@RequestParam("message") String message) {
        this.producer.sendMessage(message);

        return "success";
    }
}
