package com.example.rabbitmqlab.consumer;

import com.example.rabbitmqlab.messaging.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoQueueConsumer {

    @RabbitListener(queues = RabbitMqConstants.DEMO_QUEUE)
    public void listen(String message) {
        log.info("[DEMO-CONSUMER] Received message from '{}': {}", RabbitMqConstants.DEMO_QUEUE, message);
    }
}
