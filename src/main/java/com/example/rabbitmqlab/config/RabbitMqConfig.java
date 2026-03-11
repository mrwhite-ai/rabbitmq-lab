package com.example.rabbitmqlab.config;

import com.example.rabbitmqlab.messaging.RabbitMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMqConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue demoQueue() {
        return new Queue(RabbitMqConstants.DEMO_QUEUE, true);
    }

    @Bean
    public TopicExchange demoExchange() {
        return new TopicExchange(RabbitMqConstants.DEMO_EXCHANGE, true, false);
    }

    @Bean
    public Binding demoBinding() {
        return BindingBuilder.bind(demoQueue())
                .to(demoExchange())
                .with(RabbitMqConstants.DEMO_ROUTING_KEY);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(RabbitMqConstants.ORDER_QUEUE)
                .deadLetterExchange(RabbitMqConstants.ORDER_DLX_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.ORDER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(RabbitMqConstants.ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(RabbitMqConstants.ORDER_ROUTING_KEY);
    }

    @Bean
    public TopicExchange orderDlxExchange() {
        return new TopicExchange(RabbitMqConstants.ORDER_DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderDlq() {
        return new Queue(RabbitMqConstants.ORDER_DLQ, true);
    }

    @Bean
    public Binding orderDlqBinding() {
        return BindingBuilder.bind(orderDlq())
                .to(orderDlxExchange())
                .with(RabbitMqConstants.ORDER_DLQ_ROUTING_KEY);
    }

    @Bean
    public RetryOperationsInterceptor rabbitRetryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter,
            RetryOperationsInterceptor rabbitRetryInterceptor
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(2);
        factory.setAdviceChain(rabbitRetryInterceptor);
        return factory;
    }
}
