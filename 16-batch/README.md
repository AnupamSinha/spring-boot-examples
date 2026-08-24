# Spring Batch Demo — Processing Millions of Records

A production-grade Spring Batch application demonstrating chunk-oriented processing: reading transactions from CSV, validating/enriching them, and writing to PostgreSQL.

## Features

- **FlatFileItemReader** — reads CSV files with configurable chunk sizes
- **ItemProcessor** — validates amounts and flags suspicious transactions
- **JdbcBatchItemWriter** — bulk inserts into PostgreSQL
- **Skip & Retry** — fault-tolerant processing
- **Chunk-oriented processing** — memory-efficient handling of large datasets

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Batch
- PostgreSQL 16
- H2 (testing)

## Getting Started

```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test
```

## Blog Post

Full walkthrough: [Spring Batch — Processing Millions of Records Without Breaking a Sweat](https://anupamsinha.github.io/posts/spring-batch-processing-millions/)
