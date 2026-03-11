package com.example.rabbitmqlab.service;

import com.example.rabbitmqlab.dto.OrderCreatedEvent;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderProcessingService {

    public void processOrder(String consumerName, OrderCreatedEvent event) {
        log.info("[{}] Start processing order {}", consumerName, event.getOrderId());
        processPaymentWithRandomFailure(consumerName, event);
        sendEmail(consumerName, event);
        log.info("[{}] Finished processing order {}", consumerName, event.getOrderId());
    }

    private void processPaymentWithRandomFailure(String consumerName, OrderCreatedEvent event) {
        sleepSeconds(2);
        boolean shouldFail = ThreadLocalRandom.current().nextInt(100) < 35;
        if (shouldFail) {
            log.warn("[{}] Payment failed for order {}. Throwing exception to trigger retry.",
                    consumerName, event.getOrderId());
            throw new IllegalStateException("Payment service temporary failure for order " + event.getOrderId());
        }
        log.info("[{}] Payment completed for order {}", consumerName, event.getOrderId());
    }

    private void sendEmail(String consumerName, OrderCreatedEvent event) {
        sleepSeconds(3);
        log.info("[{}] Email sent to {} for order {}", consumerName, event.getCustomerEmail(), event.getOrderId());
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted during fake processing delay", e);
        }
    }
}
