# CQRS + Event Sourcing with Spring Boot

A demonstration of CQRS (Command Query Responsibility Segregation) and Event Sourcing patterns using Spring Boot 3.5, PostgreSQL for the read model, and MongoDB as the event store.

## Architecture

- **Command Side** — Accepts commands, validates business rules, emits domain events
- **Event Store (MongoDB)** — Persists all domain events as the source of truth
- **Projection** — Listens to events and builds the read model
- **Query Side (PostgreSQL)** — Optimized read model for queries

## Prerequisites

- Java 21
- Docker & Docker Compose

## Running

```bash
# Start infrastructure
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## API

```bash
# Create an order (command)
curl -X POST http://localhost:8080/api/commands/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": "cust-1", "items": [{"productId": "prod-1", "quantity": 2, "price": 29.99}]}'

# Query orders (read model)
curl http://localhost:8080/api/queries/orders/cust-1
```

## Blog Post

Full explanation: [CQRS + Event Sourcing with Spring Boot](https://anupamsinha.github.io/posts/spring-boot-cqrs-event-sourcing/)
