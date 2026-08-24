# Spring Boot + GraphQL

A Spring Boot application demonstrating schema-first GraphQL with `@QueryMapping`, `@MutationMapping`, `@SchemaMapping`, and N+1 prevention with `@BatchMapping`.

## Features

- Schema-first GraphQL API design
- Query and Mutation resolvers
- Nested type resolution with `@SchemaMapping`
- N+1 prevention using `@BatchMapping`
- GraphiQL UI for testing
- H2 in-memory database with JPA

## Prerequisites

- Java 21

## Quick Start

```bash
./mvnw spring-boot:run
```

Open GraphiQL at: [http://localhost:8080/graphiql](http://localhost:8080/graphiql)

## Example Queries

```graphql
# Get all products with reviews
{
  products {
    id
    name
    price
    reviews {
      rating
      comment
    }
  }
}

# Create a product
mutation {
  createProduct(input: { name: "Keyboard", price: 79.99, category: "peripherals" }) {
    id
    name
  }
}
```

## Blog Post

Full walkthrough: [Spring Boot + GraphQL — Flexible APIs for Frontend Teams](https://anupamsinha.github.io/posts/spring-boot-graphql-flexible-apis/)
