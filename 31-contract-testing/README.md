# Contract Testing with Spring Cloud Contract

Consumer-driven contract testing between microservices using Spring Cloud Contract.

## Architecture

```
31-contract-testing/
├── producer/   # Payment service (defines and verifies contracts)
├── consumer/   # Order service (uses stubs from producer contracts)
└── pom.xml     # Parent POM with shared config
```

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Spring Cloud | 2024.0.0 |
| Spring Cloud Contract | (managed by Spring Cloud BOM) |

## How It Works

1. **Consumer defines expectations** — what endpoints do we call and what do we expect back?
2. **Producer writes contracts** (Groovy DSL) — formalizes the expectations
3. **Producer builds** — auto-generates tests from contracts + generates stubs
4. **Consumer tests** — uses stub runner to test against generated stubs

## Running

### Producer (generates tests from contracts)

```bash
cd producer
mvn clean test
```

This runs the auto-generated contract verification tests.

### Consumer (uses stubs from producer)

First, install producer stubs locally:

```bash
cd producer
mvn clean install -DskipTests
```

Then run consumer tests:

```bash
cd consumer
mvn clean test
```

## Blog Post

See the full walkthrough: [Contract Testing with Spring Cloud Contract — Consumer-Driven Contracts](https://anupamsinha.github.io/java/spring/2026/08/22/spring-cloud-contract-testing.html)
