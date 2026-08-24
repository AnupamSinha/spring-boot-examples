# Spring Boot + MongoDB

A Spring Boot application demonstrating document modeling, MongoRepository with custom queries, and aggregation pipelines using MongoTemplate.

## Features

- Document modeling with embedded records and tag lists
- MongoRepository with derived queries and `@Query` annotations
- Aggregation pipelines (group by category, sum prices)
- Full CRUD REST API

## Prerequisites

- Java 21
- Docker & Docker Compose

## Quick Start

```bash
# Start MongoDB
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## API Endpoints

| Method | Endpoint                     | Description                    |
|--------|------------------------------|--------------------------------|
| GET    | /api/products                | List all products              |
| GET    | /api/products/{id}           | Get product by ID              |
| POST   | /api/products                | Create a product               |
| PUT    | /api/products/{id}           | Update a product               |
| DELETE | /api/products/{id}           | Delete a product               |
| GET    | /api/products/category/{cat} | Find by category               |
| GET    | /api/products/search?name=x  | Search by name (regex)         |
| GET    | /api/products/aggregation    | Category summary (aggregation) |

## Blog Post

Full walkthrough: [Spring Boot + MongoDB — When Documents Beat Tables](https://anupamsinha.github.io/posts/spring-boot-mongodb-documents/)
