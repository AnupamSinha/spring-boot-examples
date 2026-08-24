# Spring Boot Multi-Tenancy

A multi-tenant Spring Boot application demonstrating schema-per-tenant isolation using `AbstractRoutingDataSource`.

Each tenant gets its own PostgreSQL schema, and requests are routed to the correct schema based on the `X-Tenant-ID` header.

## Features

- Schema-per-tenant data isolation
- Dynamic tenant routing via `AbstractRoutingDataSource`
- Tenant resolution from HTTP headers
- Simple CRUD API for products

## Prerequisites

- Java 21
- Docker & Docker Compose
- Maven

## Running

```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

## Usage

```bash
# Create a product for tenant_a
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant_a" \
  -d '{"name": "Widget", "price": 9.99}'

# List products for tenant_b
curl http://localhost:8080/api/products \
  -H "X-Tenant-ID: tenant_b"
```

## Blog Post

Detailed walkthrough: [Spring Boot Multi-Tenancy — One App, Multiple Customers](https://anupamsinha.github.io/posts/spring-boot-multi-tenancy/)
