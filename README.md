# Spring Boot Examples

A collection of production-ready Spring Boot examples covering fundamentals, AI, architecture, data, security, DevOps, and system design.

**Blog**: [anupamsinha.github.io](https://anupamsinha.github.io)

---

## Projects

| # | Folder | Topic | Blog Post |
|---|--------|-------|-----------|
| 01 | [basics](./01-basics/) | Spring Boot from Zero (REST, JPA, Security) | [Beat the Basics](https://anupamsinha.github.io/posts/beat-the-basics-spring-boot/) |
| 02 | [virtual-threads](./02-virtual-threads/) | Project Loom Virtual Threads | [Virtual Threads](https://anupamsinha.github.io/posts/spring-boot-virtual-threads-project-loom/) |
| 03 | [graalvm-native](./03-graalvm-native/) | GraalVM Native Image | [GraalVM Native](https://anupamsinha.github.io/posts/spring-boot-graalvm-native-images/) |
| 04 | [performance-tuning](./04-performance-tuning/) | HikariCP, Caching, Async, Metrics | [Performance Tuning](https://anupamsinha.github.io/posts/spring-boot-performance-tuning-guide/) |
| 05 | [ai-function-calling](./05-ai-function-calling/) | Spring AI Tool Calling | [Function Calling](https://anupamsinha.github.io/posts/spring-ai-function-calling-tool-use/) |
| 06 | [ai-mcp](./06-ai-mcp/) | MCP Server + Client | [MCP](https://anupamsinha.github.io/posts/spring-ai-mcp-server-client/) |
| 07 | [ai-agents](./07-ai-agents/) | Agentic Patterns + Streaming | [Agents](https://anupamsinha.github.io/posts/spring-ai-agentic-patterns-streaming/) |
| 08 | [testcontainers](./08-testcontainers/) | Integration Testing (PostgreSQL, Kafka) | [Testcontainers](https://anupamsinha.github.io/posts/spring-boot-testcontainers-integration-testing/) |
| 09 | [security-oauth2](./09-security-oauth2/) | OAuth2/OIDC Resource Server + Keycloak | [OAuth2](https://anupamsinha.github.io/posts/spring-security-oauth2-oidc-keycloak/) |
| 10 | [kafka-microservices](./10-kafka-microservices/) | Event-Driven (Order, Payment, Notification) | [Kafka](https://anupamsinha.github.io/posts/spring-boot-kafka-event-driven-microservices/) |
| 11 | [observability](./11-observability/) | Prometheus + Loki + Tempo + Grafana | [Observability](https://anupamsinha.github.io/posts/spring-boot-observability-opentelemetry/) |
| 12 | [k8s-deploy](./12-k8s-deploy/) | Docker + Kubernetes Deployment | [K8s Deploy](https://anupamsinha.github.io/posts/spring-boot-docker-k8s-deployment/) |
| 13 | [jpa-advanced](./13-jpa-advanced/) | Specifications, Projections, Auditing | [JPA Advanced](https://anupamsinha.github.io/posts/spring-data-jpa-beyond-basics/) |
| 14 | [grpc](./14-grpc/) | gRPC Server + Client | [gRPC](https://anupamsinha.github.io/posts/spring-boot-grpc-high-performance/) |
| 15 | [caching](./15-caching/) | Caffeine L1 + Redis L2 | [Caching](https://anupamsinha.github.io/posts/spring-boot-caching-strategies/) |
| 16 | [batch](./16-batch/) | Spring Batch CSV → DB | [Batch](https://anupamsinha.github.io/posts/spring-batch-processing-millions/) |
| 17 | [websocket](./17-websocket/) | WebSocket + STOMP Chat | [WebSocket](https://anupamsinha.github.io/posts/spring-boot-websocket-real-time/) |
| 18 | [multi-tenancy](./18-multi-tenancy/) | Schema-per-Tenant Routing | [Multi-Tenancy](https://anupamsinha.github.io/posts/spring-boot-multi-tenancy/) |
| 19 | [spring-shell](./19-spring-shell/) | Interactive CLI App | [Spring Shell](https://anupamsinha.github.io/posts/spring-shell-cli-application/) |
| 20 | [feature-flags](./20-feature-flags/) | Togglz Runtime Toggles | [Feature Flags](https://anupamsinha.github.io/posts/spring-boot-feature-flags/) |
| 21 | [cqrs-eventsourcing](./21-cqrs-eventsourcing/) | CQRS + Event Sourcing | [CQRS](https://anupamsinha.github.io/posts/spring-boot-cqrs-event-sourcing/) |
| 22 | [hexagonal](./22-hexagonal/) | Hexagonal Architecture | [Hexagonal](https://anupamsinha.github.io/posts/spring-boot-hexagonal-architecture/) |
| 23 | [mongodb](./23-mongodb/) | MongoDB + Aggregations | [MongoDB](https://anupamsinha.github.io/posts/spring-boot-mongodb-documents/) |
| 24 | [graphql](./24-graphql/) | GraphQL Schema-First | [GraphQL](https://anupamsinha.github.io/posts/spring-boot-graphql-flexible-apis/) |
| 25 | [outbox-pattern](./25-outbox-pattern/) | Transactional Outbox + Kafka | [Outbox](https://anupamsinha.github.io/posts/spring-boot-outbox-pattern/) |
| 26 | [rate-limiter](./26-rate-limiter/) | Redis Sliding Window | [Rate Limiter](https://anupamsinha.github.io/posts/system-design-rate-limiter-spring-boot/) |
| 27 | [url-shortener](./27-url-shortener/) | Base62 URL Shortener | [URL Shortener](https://anupamsinha.github.io/posts/system-design-url-shortener-spring-boot/) |
| 28 | [notification-service](./28-notification-service/) | Multi-Channel (Email/SMS/Push) | [Notifications](https://anupamsinha.github.io/posts/system-design-notification-service-spring-boot/) |

---

## Tech Stack

All projects use:
- **Java 21**
- **Spring Boot 3.5.0**
- **Maven**

Individual projects may add: PostgreSQL, Redis, Kafka, MongoDB, Keycloak, Grafana stack, etc. — each has its own `docker-compose.yml` where needed.

---

## Quick Start

```bash
git clone https://github.com/AnupamSinha/spring-boot-examples.git
cd spring-boot-examples

# Run any project
cd 01-basics
./mvnw spring-boot:run

# Or with Docker dependencies
cd 15-caching
docker compose up -d
./mvnw spring-boot:run
```

---

## License

MIT
