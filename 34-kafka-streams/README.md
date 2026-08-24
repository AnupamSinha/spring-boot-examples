# 34 — Spring Boot + Kafka Streams Real-Time Processing

Demonstrates real-time stream processing with Spring Boot and Kafka Streams including topology building, windowed aggregations, state stores, and interactive queries.

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Kafka + Kafka Streams
- Apache Kafka 3.8 (KRaft mode)

## Running

### 1. Start Kafka

```bash
docker-compose up -d
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

The application automatically produces test payment events every 2 seconds and processes them through the Kafka Streams topology.

### 3. Query the State Store

```bash
# Get payment counts per currency (last 1-minute window)
curl http://localhost:8080/api/streams/counts

# Check stream processor status
curl http://localhost:8080/api/streams/status
```

## Stream Topology

```
raw-payments topic
    → filter (amount > 0)
    → mapValues (enrich with timestamp + status)
    → groupBy (currency)
    → windowedBy (tumbling 1-minute)
    → count
    → payment-counts topic
```

## API Endpoints

| Method | Endpoint              | Description                              |
|--------|-----------------------|------------------------------------------|
| GET    | /api/streams/counts   | Payment counts per currency (last window) |
| GET    | /api/streams/status   | Kafka Streams processor status           |
