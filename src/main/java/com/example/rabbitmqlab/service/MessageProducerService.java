package com.example.rabbitmqlab.service;

import com.example.rabbitmqlab.dto.OrderCreatedEvent;
import com.example.rabbitmqlab.messaging.RabbitMqConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendDemoMessage(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMqConstants.DEMO_EXCHANGE,
                RabbitMqConstants.DEMO_ROUTING_KEY,
                message,
                rabbitMessage -> {
                    rabbitMessage.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return rabbitMessage;
                }
        );
        log.info("[PRODUCER] Sent demo message to exchange='{}', routingKey='{}': {}",
                RabbitMqConstants.DEMO_EXCHANGE, RabbitMqConstants.DEMO_ROUTING_KEY, message);
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMqConstants.ORDER_EXCHANGE,
                RabbitMqConstants.ORDER_ROUTING_KEY,
                event,
                rabbitMessage -> {
                    rabbitMessage.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return rabbitMessage;
                }
        );
        log.info("[PRODUCER] Sent order event: orderId={}, amount={}, email={}",
                event.getOrderId(), event.getAmount(), event.getCustomerEmail());
    }
}
