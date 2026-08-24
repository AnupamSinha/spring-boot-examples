# 35 — Spring Boot Scheduling with Distributed Locks

Demonstrates job scheduling in Spring Boot with `@Scheduled`, custom thread pools, and ShedLock for distributed lock management in clustered environments.

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA + H2
- ShedLock (JDBC provider)

## Running

```bash
./mvnw spring-boot:run
```

No external dependencies needed — uses H2 in-memory database for ShedLock storage.

## Scheduled Tasks

| Task | Schedule | Lock |
|------|----------|------|
| Report Generation | Cron: daily at 2 AM | ShedLock (5m min, 30m max) |
| Data Cleanup | fixedRate: every hour | ShedLock (5m min, 55m max) |
| Health Check | fixedDelay: every 30s | No lock (each instance runs independently) |

## API Endpoints

| Method | Endpoint                     | Description                    |
|--------|------------------------------|--------------------------------|
| POST   | /api/tasks/trigger/{name}    | Manually trigger a task        |
| GET    | /api/tasks/status            | Show last run times and status |

### Manual Trigger

```bash
# Trigger report generation
curl -X POST http://localhost:8080/api/tasks/trigger/report

# Trigger data cleanup
curl -X POST http://localhost:8080/api/tasks/trigger/cleanup

# Trigger health check
curl -X POST http://localhost:8080/api/tasks/trigger/healthcheck
```

### Check Status

```bash
curl http://localhost:8080/api/tasks/status
```

## ShedLock Table

The `shedlock` table is auto-created via `schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS shedlock (
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
```
