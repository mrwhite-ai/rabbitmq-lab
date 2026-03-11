package com.example.rabbitmqlab.messaging;

public final class RabbitMqConstants {

    private RabbitMqConstants() {
    }

    public static final String DEMO_QUEUE = "demo.queue";
    public static final String DEMO_EXCHANGE = "demo.exchange";
    public static final String DEMO_ROUTING_KEY = "demo.key";

    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.created";

    public static final String ORDER_DLX_EXCHANGE = "order.dlx";
    public static final String ORDER_DLQ = "order.dlq";
    public static final String ORDER_DLQ_ROUTING_KEY = "order.dlq";
}
