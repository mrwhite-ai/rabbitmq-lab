package com.example.rabbitmqlab.consumer;

import com.example.rabbitmqlab.dto.OrderCreatedEvent;
import com.example.rabbitmqlab.messaging.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderDlqConsumer {

    @RabbitListener(queues = RabbitMqConstants.ORDER_DLQ)
    public void consumeFailed(OrderCreatedEvent event) {
        log.error("[DLQ-CONSUMER] Message moved to DLQ '{}'. orderId={}, email={}, amount={}",
                RabbitMqConstants.ORDER_DLQ, event.getOrderId(), event.getCustomerEmail(), event.getAmount());
    }
}
