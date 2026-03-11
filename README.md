# RabbitMQ Lab (Spring Boot 3, Java 21)

This project is designed as a **progressive learning path**.
You start from zero RabbitMQ usage and gradually move to retry, DLQ, and durability.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring AMQP
- RabbitMQ
- Docker
- Maven

## Project Structure

- `controller`
- `service`
- `messaging`
- `consumer`
- `config`
- `dto`

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker + Docker Compose

---

## PHASE 0 - Environment setup

Only RabbitMQ runs in Docker.

File: `docker-compose.yml`

```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq-lab
    ports:
      - "5672:5672"
      - "15672:15672"
```

### Start RabbitMQ

```bash
docker compose up -d
```

### Open dashboard

- URL: [http://localhost:15672](http://localhost:15672)
- Username: `guest`
- Password: `guest`

No Spring Boot messaging in this phase.

---

## PHASE 1 - Simple Spring Boot app

Goal: ensure project boots correctly.

### Run app

```bash
mvn spring-boot:run
```

### Test endpoint

```bash
curl http://localhost:8080/hello
```

Expected response:

```text
Hello from Spring Boot + RabbitMQ lab
```

No RabbitMQ action required yet.

---

## PHASE 2 - First RabbitMQ connection

Goal: connect Spring Boot to RabbitMQ and publish to `demo.queue`.

Connection is configured in `src/main/resources/application.yml`.

`POST /send` publishes a string message.

### Test

```bash
curl -X POST http://localhost:8080/send \
  -H "Content-Type: application/json" \
  -d '{"message":"hello rabbitmq"}'
```

At this point, you can verify in RabbitMQ UI that messages are being published.

---

## PHASE 3 - First Consumer

Goal: consume from `demo.queue` with `@RabbitListener`.

Consumer class: `consumer/DemoQueueConsumer`.

When a message arrives, it is logged in console.

Flow is now:

`API -> RabbitMQ -> Consumer`

---

## PHASE 4 - Understanding Exchanges

RabbitMQ concepts:

- **Queue**: where messages wait.
- **Exchange**: receives messages from producers and routes them.
- **Routing Key**: routing hint used by exchange/bindings.

Current config:

- Exchange: `demo.exchange`
- Queue: `demo.queue`
- Routing Key: `demo.key`

Producer sends to exchange, not directly to queue.

---

## PHASE 5 - Simulating real async work

Goal: model a simple order workflow.

### Endpoint

`POST /orders`

Example:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orderId":"ORD-1001",
    "customerEmail":"furkan@example.com",
    "amount":149.90
  }'
```

Producer sends `OrderCreatedEvent`.

Consumer processing simulates:

- payment: 2 seconds
- email: 3 seconds

You should see that HTTP request returns quickly (`202 Accepted`) while logs continue in background.

---

## PHASE 6 - Multiple Consumers

There are two consumers on `order.queue`:

- `OrderConsumerOne`
- `OrderConsumerTwo`

RabbitMQ distributes messages between active consumers (competing consumers pattern).

### Load test quickly

Run this multiple times:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-2001","customerEmail":"a@example.com","amount":50.0}'
```

Then change `orderId` and send many requests. Logs will alternate between consumers.

---

## PHASE 7 - Retry mechanism

Retry is configured for listeners:

- max attempts: `3`

`OrderProcessingService` randomly fails payment to demonstrate retry behavior.

When payment fails, you will see repeated processing attempts in logs.

---

## PHASE 8 - Dead Letter Queue (DLQ)

If retries fail, message is rejected and moved to DLQ.

Configured:

- DLQ: `order.dlq`
- DLX exchange: `order.dlx`

DLQ consumer: `OrderDlqConsumer` logs failed messages.

This helps you inspect and replay failures later.

---

## PHASE 9 - Message durability

Durability is enabled:

- durable queues
- durable exchanges
- persistent messages from producer

Meaning:

- if RabbitMQ restarts, durable queues/exchanges survive
- persistent messages are stored and can survive broker restart (if not yet consumed)

---

## Useful RabbitMQ UI checks

In Management UI:

- `Queues and Streams` tab:
  - `demo.queue`
  - `order.queue`
  - `order.dlq`
- `Exchanges` tab:
  - `demo.exchange`
  - `order.exchange`
  - `order.dlx`

---

## Quick Run Guide

1. Start RabbitMQ:
   ```bash
   docker compose up -d
   ```
2. Start Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
3. Test:
   - `GET /hello`
   - `POST /send`
   - `POST /orders`
4. Watch application logs and RabbitMQ dashboard.
