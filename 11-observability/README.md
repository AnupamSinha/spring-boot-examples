# Spring Boot Observability — Metrics, Logs, Traces

Complete observability stack with Prometheus (metrics), Loki (logs), Tempo (traces), and Grafana (visualization).

> Blog post: [Spring Boot Observability Stack — Metrics, Logs, Traces with OpenTelemetry](https://anupamsinha.github.io/posts/spring-boot-observability-opentelemetry/)

## Architecture

```
Spring Boot App ──── metrics ────▶ Prometheus ───┐
       │                                          │
       ├──── logs (with traceId) ────▶ Loki ─────┼───▶ Grafana (localhost:3000)
       │                                          │
       └──── traces (OTLP) ────▶ Tempo ──────────┘
```

## Quick Start

```bash
# 1. Start the observability stack
docker compose up -d

# 2. Run the app
./mvnw spring-boot:run

# 3. Generate traffic
curl http://localhost:8080/api/payments/PAY-001
curl -X POST "http://localhost:8080/api/payments?from=alice&to=bob&amount=250"
curl http://localhost:8080/api/payments/slow
curl http://localhost:8080/api/payments/error

# 4. Open Grafana
open http://localhost:3000   # admin/admin
```

## Endpoints

| Endpoint | Purpose |
|----------|---------|
| `POST /api/payments?from=X&to=Y&amount=Z` | Process a payment (custom metrics + spans) |
| `GET /api/payments/{id}` | Lookup a payment |
| `GET /api/payments/slow` | Simulate slow response (2s) |
| `GET /api/payments/error` | Simulate 500 error |
| `GET /actuator/prometheus` | Prometheus metrics endpoint |

## Observability Stack

| Tool | Port | Role |
|------|------|------|
| Prometheus | 9090 | Scrapes metrics from `/actuator/prometheus` |
| Loki | 3100 | Receives structured logs with trace IDs |
| Tempo | 4317/4318 | Receives traces via OTLP |
| Grafana | 3000 | Unified dashboards (admin/admin) |

## Key Metrics (auto + custom)

- `http_server_requests_seconds` — request latency by endpoint
- `payments.processed.total` — custom counter for processed payments
- `payments.processing.duration` — custom timer for processing time
- `jvm_memory_used_bytes` — JVM heap usage

## Correlation Flow

1. Prometheus alert fires on high error rate
2. Click → Loki shows error log lines with `traceId`
3. Click traceId → Tempo shows the full request trace with spans

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Micrometer | Latest |
| OpenTelemetry | OTLP exporter |
| Grafana | 11.1.0 |
| Prometheus | 2.53.0 |
| Loki | 3.1.0 |
| Tempo | 2.5.0 |

## License

MIT
