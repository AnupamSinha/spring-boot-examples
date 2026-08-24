# Spring Boot Outbox Pattern + Kafka

Implements the Transactional Outbox Pattern to solve the dual-write problem. Domain events are saved to an outbox table in the same database transaction, then relayed to Kafka asynchronously — guaranteeing at-least-once delivery without distributed transactions.

## Features

- Transactional outbox: entity + event saved atomically
- Polling-based relay with `@Scheduled`
- Kafka event publishing
- Idempotent event processing
- PostgreSQL for persistence

## Prerequisites

- Java 21
- Docker & Docker Compose

## Quick Start

```bash
# Start PostgreSQL and Kafka
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## API Endpoints

| Method | Endpoint        | Description      |
|--------|-----------------|------------------|
| POST   | /api/orders     | Create an order  |
| GET    | /api/orders     | List all orders  |
| GET    | /api/orders/{id}| Get order by ID  |

## How It Works

1. `OrderService` saves the `Order` and an `OutboxEvent` in the same `@Transactional` method
2. `OutboxRelay` polls for unpublished events every 5 seconds
3. Events are sent to Kafka topic `order-events`
4. Events are marked as published after successful send

## Blog Post

Full walkthrough: [Outbox Pattern with Spring Boot + Kafka](https://anupamsinha.github.io/posts/spring-boot-outbox-pattern/)
