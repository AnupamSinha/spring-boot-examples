# URL Shortener — Spring Boot

A production-style URL shortener built with Spring Boot 3.5, Java 21, and PostgreSQL. Uses Base62 encoding from auto-increment IDs for short code generation.

## Features

- **Shorten URLs** — POST `/api/shorten` returns a short URL
- **Redirect** — GET `/{code}` performs a 302 redirect to the original URL
- **Click Analytics** — GET `/api/stats/{code}` returns click count and metadata
- **URL Expiration** — Optional TTL for shortened URLs
- **Base62 Encoding** — Deterministic, collision-free short codes from database IDs

## Tech Stack

| Layer       | Technology         |
|-------------|--------------------|
| Framework   | Spring Boot 3.5.0  |
| Language    | Java 21            |
| Database    | PostgreSQL 16      |
| Build       | Maven              |
| Monitoring  | Spring Actuator    |

## Running Locally

```bash
# Start PostgreSQL
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## API Usage

```bash
# Shorten a URL
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://example.com/very-long-url", "expiresInDays": 30}'

# Redirect
curl -L http://localhost:8080/abc123

# Get stats
curl http://localhost:8080/api/stats/abc123
```

## Blog Post

Detailed system design walkthrough:  
[System Design — URL Shortener with Spring Boot](https://anupamsinha.github.io/posts/system-design-url-shortener-spring-boot/)
