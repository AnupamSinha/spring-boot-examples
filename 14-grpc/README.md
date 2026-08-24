# Spring Boot + gRPC

High-performance inter-service communication using Spring Boot 3.5 and gRPC.

## Modules

| Module | Description |
|--------|-------------|
| `proto` | Protocol Buffer definitions and generated gRPC stubs |
| `grpc-server` | gRPC server exposing `PaymentService` |
| `grpc-client` | REST API gateway that calls gRPC server internally |

## Prerequisites

- Java 21+
- Maven 3.9+

## Build

```bash
mvn clean install
```

## Run

Start the gRPC server:

```bash
cd grpc-server
mvn spring-boot:run
```

Start the REST client (in a separate terminal):

```bash
cd grpc-client
mvn spring-boot:run
```

## Test

```bash
# Get a single payment
curl http://localhost:8080/api/payments/PAY-001

# List payments for a user (server streaming)
curl "http://localhost:8080/api/payments?userId=USER-1&pageSize=10"
```

## Blog Post

Full walkthrough: [Spring Boot + gRPC — High-Performance Inter-Service Communication](https://anupamsinha.github.io/posts/spring-boot-grpc-high-performance/)
