# Spring Boot with Virtual Threads — Project Loom in Practice

Companion project for the blog post: [Spring Boot with Virtual Threads — Project Loom in Practice](https://anupamsinha.github.io/posts/spring-boot-virtual-threads-project-loom/)

Demonstrates how to enable and leverage Java 21 virtual threads in a Spring Boot application for improved concurrency and throughput.

## Quick Start

```bash
./mvnw spring-boot:run
```

The app starts on **http://localhost:8080**.

## How to Enable Virtual Threads

Virtual threads are enabled via a single property in `application.yml`:

```yaml
spring:
  threads:
    virtual:
      enabled: true
```

Additionally, a custom `TomcatProtocolHandlerCustomizer` bean configures the embedded Tomcat server to use virtual threads for request handling.

## API Endpoints

| Method | Endpoint                      | Description                                                |
|--------|-------------------------------|------------------------------------------------------------|
| GET    | /api/blocking                 | Simulates a 500ms blocking IO operation                    |
| GET    | /api/thread-info              | Returns current thread name and whether it's virtual       |
| GET    | /api/parallel                 | Runs 10 blocking operations concurrently using virtual threads |
| GET    | /api/benchmark?requests=100   | Benchmarks N concurrent requests with platform vs virtual threads |

## Benchmark Instructions

1. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Run a benchmark with 100 concurrent requests:
   ```bash
   curl "http://localhost:8080/api/benchmark?requests=100"
   ```

3. Compare the response showing platform thread time vs virtual thread time.

4. For load testing with external tools:
   ```bash
   # Using Apache Bench
   ab -n 1000 -c 200 http://localhost:8080/api/blocking

   # Using hey
   hey -n 1000 -c 200 http://localhost:8080/api/blocking
   ```

## Actuator Endpoints

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Thread info: http://localhost:8080/actuator/metrics/jvm.threads.live

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Virtual Threads (Project Loom)
- Spring Actuator
