# Event-Driven Microservices with Spring Boot + Kafka

Three microservices communicating via Apache Kafka events.

> Blog post: [Event-Driven Microservices with Spring Boot + Kafka](https://anupamsinha.github.io/posts/spring-boot-kafka-event-driven-microservices/)

## Architecture

```
Order Service (8080) ──── OrderCreatedEvent ────▶ [orders topic]
                                                        │
                                              ┌─────────┴──────────┐
                                              ▼                    ▼
                                    Payment Service (8081)  Notification Service (8082)
                                              │
                                    PaymentCompletedEvent
                                              │
                                              ▼
                                       [payments topic]
                                              │
                                              ▼
                                    Notification Service
```

## Quick Start

```bash
# 1. Start Kafka
docker compose up -d

# 2. Build shared events
./mvnw install -pl shared-events

# 3. Run all services (separate terminals)
./mvnw spring-boot:run -pl order-service
./mvnw spring-boot:run -pl payment-service
./mvnw spring-boot:run -pl notification-service

# 4. Create an order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": "CUST-42", "items": [{"productId": "P1", "name": "Widget", "quantity": 2, "price": 49.99}]}'
```

## Modules

| Module | Port | Role |
|--------|------|------|
| shared-events | — | Event record definitions |
| order-service | 8080 | REST API → publishes OrderCreatedEvent |
| payment-service | 8081 | Consumes orders → publishes PaymentCompletedEvent |
| notification-service | 8082 | Consumes both events → logs notifications |

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Apache Kafka | Confluent 7.6.0 (KRaft) |

## License

MIT
