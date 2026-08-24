# Spring Boot Caching — Multi-Level with Caffeine & Redis

Demonstrates multi-level caching in Spring Boot using **Caffeine** as L1 (in-memory) and **Redis** as L2 (distributed).

## Quick Start

```bash
# Start Redis
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## Architecture

- **L1 — Caffeine**: In-process cache, 60s TTL, max 500 entries
- **L2 — Redis**: Distributed cache, 10min TTL, shared across instances

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/products/{id}` | Get product (cached) |
| GET | `/api/products` | List all products |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product (evicts cache) |
| DELETE | `/api/products/{id}` | Delete product (evicts cache) |

## Blog Post

Full walkthrough: [Caching Strategies with Spring Boot](https://anupamsinha.github.io/posts/spring-boot-caching-strategies/)
