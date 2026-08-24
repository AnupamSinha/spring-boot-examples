# Spring Boot Performance Tuning — A Practical Guide

This project demonstrates practical performance tuning techniques for Spring Boot applications including connection pooling, caching, async processing, and JVM optimization.

📖 **Blog Post**: [Spring Boot Performance Tuning Guide](https://anupamsinha.github.io/posts/spring-boot-performance-tuning-guide/)

## Features

- HikariCP connection pool tuning with leak detection
- Caffeine caching with statistics
- Async processing with custom thread pool
- JPA batch operations and N+1 prevention
- Micrometer metrics with Prometheus export
- Custom performance metrics endpoint

## Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose (for PostgreSQL)

## Quick Start

1. Start PostgreSQL:

```bash
docker-compose up -d
```

2. Run the application:

```bash
./mvnw spring-boot:run
```

3. Run with performance profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=perf
```

## JVM Tuning Flags

For production deployments, consider these JVM flags:

```bash
java -jar target/spring-boot-performance-tuning-0.0.1-SNAPSHOT.jar \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -Xms512m \
  -Xmx1024m \
  -XX:+UseStringDeduplication \
  -XX:+OptimizeStringConcat \
  -XX:MaxGCPauseMillis=50
```

## Connection Pool Tuning

HikariCP is configured with optimized settings:

- **Maximum Pool Size**: 20 (based on formula: connections = (core_count * 2) + spindle_count)
- **Minimum Idle**: 10 (keep connections warm)
- **Connection Timeout**: 20s (fail fast)
- **Leak Detection**: 30s threshold

## API Endpoints

| Endpoint                        | Description                          |
|---------------------------------|--------------------------------------|
| `/api/products`                 | List all products (cached)           |
| `/api/products/{id}`            | Get product by ID (cached)           |
| `/api/products/uncached`        | List products without cache          |
| `/api/products/async`           | Async product fetch                  |
| `/api/metrics/summary`          | Custom performance metrics summary   |
| `/actuator/prometheus`          | Prometheus metrics                   |
| `/actuator/health`              | Health check                         |
