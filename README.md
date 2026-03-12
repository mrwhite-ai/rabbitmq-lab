# RabbitMQ Lab

Production-oriented messaging demo built with Spring Boot and RabbitMQ.  
This project showcases asynchronous communication patterns commonly used in backend systems.

## Why this project

This lab demonstrates how to design and operate a resilient message-driven workflow:

- Publish/consume flow with RabbitMQ exchanges, routing keys, and queues
- Competing consumers for parallel processing
- Retry strategy for transient failures
- Dead Letter Queue (DLQ) handling for failed events
- Durable queues/exchanges and persistent messages
- Basic observability with Actuator, Prometheus, and Grafana

## Tech Stack

- Java 21
- Spring Boot 3
- Spring AMQP
- RabbitMQ (Management UI)
- Maven
- Docker Compose
- Prometheus + Grafana

## Architecture (high level)

1. Client sends an HTTP request (`/send` or `/orders`).
2. Producer publishes message to RabbitMQ exchange.
3. RabbitMQ routes message to target queue via routing key.
4. One consumer processes each message.
5. On failure, listener retries; after max attempts, message is moved to DLQ.

## Key capabilities demonstrated

- Understanding of asynchronous system design
- Messaging reliability patterns (retry, DLQ, durability)
- Clean Spring Boot modular structure (`controller`, `service`, `consumer`, `config`)
- Practical monitoring setup for backend services
- Ability to model realistic workflow behavior (order processing simulation)

## Quick Start

### Prerequisites

- JDK 21
- Maven 3.9+
- Docker + Docker Compose

### Run locally

```bash
docker compose up -d
mvn spring-boot:run
```

### Access points

- App: `http://localhost:8080`
- RabbitMQ UI: `http://localhost:15672` (`guest` / `guest`)
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000` (`admin` / `admin`)

### Sample requests

```bash
curl http://localhost:8080/hello

curl -X POST http://localhost:8080/send \
  -H "Content-Type: application/json" \
  -d '{"message":"hello rabbitmq"}'

curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-1001","customerEmail":"candidate@example.com","amount":149.90}'
```