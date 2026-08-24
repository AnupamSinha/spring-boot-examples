# Spring Data JPA — Beyond the Basics

Companion repository for the blog post:  
[**Spring Data JPA — Beyond the Basics (Projections, Specifications, Auditing)**](https://anupamsinha.github.io/posts/spring-data-jpa-beyond-basics/)

This project demonstrates advanced Spring Data JPA features including:

- **Specifications** — composable, type-safe dynamic queries
- **Projections** — interface-based and class-based, return only the columns you need
- **Auditing** — automatic `createdAt` / `updatedAt` timestamps via `@EntityListeners`

## Tech Stack

| Layer        | Technology             |
|--------------|------------------------|
| Language     | Java 21                |
| Framework    | Spring Boot 3.5.0      |
| Persistence  | Spring Data JPA        |
| Database     | PostgreSQL 16          |
| Testing      | Testcontainers + JUnit 5 |

## Quick Start

```bash
# Start PostgreSQL
docker compose up -d

# Run the application
./mvnw spring-boot:run

# Query products with dynamic filters
curl "http://localhost:8080/api/products?category=electronics&minPrice=100&maxPrice=500"
```

## Running Tests

```bash
./mvnw test
```

> Tests use Testcontainers — Docker must be running.

## License

MIT
