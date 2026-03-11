package com.example.rabbitmqlab.controller;

import com.example.rabbitmqlab.dto.OrderCreatedEvent;
import com.example.rabbitmqlab.dto.OrderRequest;
import com.example.rabbitmqlab.service.MessageProducerService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MessageProducerService messageProducerService;

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                request.getOrderId(),
                request.getCustomerEmail(),
                request.getAmount(),
                Instant.now()
        );
        messageProducerService.sendOrderCreatedEvent(event);
        return ResponseEntity.accepted().body("Order accepted. Processing asynchronously.");
    }
}
