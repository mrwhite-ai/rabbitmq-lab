package com.example.rabbitmqlab.controller;

import com.example.rabbitmqlab.dto.SendMessageRequest;
import com.example.rabbitmqlab.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageProducerService messageProducerService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody SendMessageRequest request) {
        messageProducerService.sendDemoMessage(request.getMessage());
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }
}
