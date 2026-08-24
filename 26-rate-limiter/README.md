# Rate Limiter — Spring Boot + Redis

A distributed rate limiter implementation using Spring Boot, custom AOP annotations, and Redis sorted sets (sliding window algorithm).

## Features

- Custom `@RateLimit` annotation for declarative rate limiting
- Sliding window algorithm using Redis sorted sets
- Client identification by IP address
- Proper HTTP 429 responses with rate limit headers
- Configurable requests and time window per endpoint

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data Redis
- Spring AOP
- Redis 7

## Running

```bash
# Start Redis
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## Endpoints

| Endpoint | Rate Limit | Description |
|----------|-----------|-------------|
| GET /api/public | None | Unrestricted endpoint |
| GET /api/limited | 10 req / 60s | Standard rate limit |
| GET /api/strict | 3 req / 10s | Strict rate limit |

## Blog Post

Full explanation and system design walkthrough:
[https://anupamsinha.github.io/posts/system-design-rate-limiter-spring-boot/](https://anupamsinha.github.io/posts/system-design-rate-limiter-spring-boot/)
