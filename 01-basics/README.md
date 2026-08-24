# Beat the Basics — Spring Boot from Zero to Confident

Companion project for the blog post: [Beat the Basics — Spring Boot from Zero to Confident](https://anupamsinha.github.io/posts/beat-the-basics-spring-boot/)

A complete Spring Boot REST API demonstrating CRUD operations, JPA, validation, exception handling, and Spring Security.

## Quick Start

```bash
# Clone and run with H2 (default profile)
./mvnw spring-boot:run

# Run with PostgreSQL (start DB first)
docker-compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

The app starts on **http://localhost:8080**.

H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:basicsdb`)

## API Endpoints

| Method | Endpoint               | Description          | Auth Required |
|--------|------------------------|----------------------|---------------|
| GET    | /api/products          | List all products    | No            |
| GET    | /api/products/{id}     | Get product by ID    | No            |
| POST   | /api/products          | Create a product     | No            |
| PUT    | /api/products/{id}     | Update a product     | No            |
| DELETE | /api/products/{id}     | Delete a product     | No            |
| GET    | /api/products/category/{category} | Filter by category | No  |
| *      | /api/admin/**          | Admin endpoints      | Yes           |

## Running Tests

```bash
./mvnw test
```

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Security
- Bean Validation
- H2 (dev) / PostgreSQL 16 (prod)
