# Spring Boot with GraalVM Native Images — Instant Startup

This project demonstrates how to compile a Spring Boot application into a GraalVM native image for near-instant startup times.

📖 **Blog Post**: [Spring Boot with GraalVM Native Images](https://anupamsinha.github.io/posts/spring-boot-graalvm-native-images/)

## Features

- Native image compilation with Spring Boot 3.5 and GraalVM
- Reflection hints using `@RegisterReflectionForBinding` and `RuntimeHintsRegistrar`
- Startup time measurement and comparison
- Actuator health endpoints in native mode

## Prerequisites

- Java 21 (GraalVM distribution recommended for native builds)
- Maven 3.9+

## Building & Running

### JVM Mode (Standard)

```bash
./mvnw clean package
java -jar target/spring-boot-graalvm-native-0.0.1-SNAPSHOT.jar
```

### Native Image

```bash
./mvnw clean package -Pnative
./target/spring-boot-graalvm-native
```

## Startup Time Comparison

| Mode          | Typical Startup Time |
|---------------|---------------------|
| JVM           | ~1.5–2.5 seconds    |
| Native Image  | ~0.03–0.08 seconds  |

Check actual startup time at runtime:

```bash
curl http://localhost:8080/api/startup
```

## API Endpoints

| Endpoint        | Description                              |
|-----------------|------------------------------------------|
| `/api/greet`    | Simple greeting response                 |
| `/api/reflect`  | Demonstrates reflection in native mode   |
| `/api/startup`  | Returns application startup duration     |
| `/actuator/health` | Health check                          |
