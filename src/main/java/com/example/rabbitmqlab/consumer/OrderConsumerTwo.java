package com.example.rabbitmqlab.consumer;

import com.example.rabbitmqlab.dto.OrderCreatedEvent;
import com.example.rabbitmqlab.messaging.RabbitMqConstants;
import com.example.rabbitmqlab.service.OrderProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumerTwo {

    private final OrderProcessingService orderProcessingService;

    @RabbitListener(queues = RabbitMqConstants.ORDER_QUEUE, id = "order-consumer-2")
    public void consume(OrderCreatedEvent event) {
        log.info("[CONSUMER-2] Message received for order {}", event.getOrderId());
        orderProcessingService.processOrder("CONSUMER-2", event);
    }
}
