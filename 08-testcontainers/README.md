# Spring Boot + Testcontainers — Integration Testing Done Right

Integration tests using real Docker containers instead of mocks or in-memory databases.

> Blog post: [Spring Boot + Testcontainers — Integration Testing Done Right](https://anupamsinha.github.io/posts/spring-boot-testcontainers-integration-testing/)

## What's Tested

| Container | Tests |
|-----------|-------|
| PostgreSQL 16 | JPA repository CRUD, custom queries, status filtering |
| Kafka (Confluent 7.6) | Producer/consumer event flow, multi-message consumption |

## Quick Start

```bash
# Docker must be running
git clone https://github.com/AnupamSinha/spring-boot-testcontainers.git
cd spring-boot-testcontainers
./mvnw verify
```

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Testcontainers | Latest (via Spring Boot BOM) |
| PostgreSQL | 16 (alpine) |
| Kafka | Confluent 7.6.0 |

## License

MIT
