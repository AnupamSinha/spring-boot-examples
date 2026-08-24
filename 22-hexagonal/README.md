# Hexagonal Architecture with Spring Boot

A demonstration of Hexagonal Architecture (Ports & Adapters) using Spring Boot 3.5 and Java 21.

## Architecture

```
┌─────────────────────────────────────────────────┐
│                  Adapters (IN)                   │
│         REST Controller, CLI, Events            │
├─────────────────────────────────────────────────┤
│                 Ports (IN)                       │
│     CreateOrderUseCase, GetOrderUseCase         │
├─────────────────────────────────────────────────┤
│                Domain Core                      │
│        Order, OrderStatus, OrderService         │
├─────────────────────────────────────────────────┤
│                 Ports (OUT)                      │
│     OrderRepository, PaymentGateway             │
├─────────────────────────────────────────────────┤
│                 Adapters (OUT)                   │
│      JPA Repository, Stripe Gateway             │
└─────────────────────────────────────────────────┘
```

## Key Principles

- **Domain is framework-free** — No Spring annotations in the domain layer
- **Dependency Rule** — All arrows point inward toward the domain
- **Ports are interfaces** — Defined by the domain, implemented by adapters
- **Testability** — Domain logic tested without Spring context

## Running

```bash
./mvnw spring-boot:run
```

## Blog Post

Full explanation: [Hexagonal Architecture with Spring Boot](https://anupamsinha.github.io/posts/spring-boot-hexagonal-architecture/)
