# Load Testing Spring Boot with Gatling

Find your Spring Boot application's breaking point with Gatling load tests.

## Architecture

```
32-gatling/
├── app/        # Spring Boot target application (port 8080)
├── gatling/    # Gatling simulations (Scala)
└── pom.xml     # Parent POM
```

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Gatling | 3.11.5 |
| Scala | 2.13.14 |

## Running

### 1. Start the target application

```bash
cd app
mvn spring-boot:run
```

### 2. Run load tests

```bash
cd gatling
mvn gatling:test -Dgatling.simulationClass=simulations.ProductApiSimulation
```

Or run the stress test:

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.StressTestSimulation
```

### 3. View HTML reports

Reports are generated at `gatling/target/gatling/` — open the `index.html` file.

## Simulations

| Simulation | Description | Profile |
|-----------|-------------|---------|
| `ProductApiSimulation` | Normal load test | Ramp 100 users over 30s |
| `StressTestSimulation` | Find breaking point | 50 users/sec constant for 60s + spike |

## Assertions

- 95th percentile response time < 1000ms
- Success rate > 95%

## Blog Post

See the full walkthrough: [Load Testing Spring Boot with Gatling — Find Your Breaking Point](https://anupamsinha.github.io/java/spring/2026/08/22/spring-boot-gatling-load-testing.html)
