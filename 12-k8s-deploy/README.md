# Spring Boot + Docker/Kubernetes — From JAR to Production

Companion code for the blog post:
[Spring Boot + Docker/Kubernetes Deployment Guide](https://anupamsinha.github.io/posts/spring-boot-docker-k8s-deployment/)

## Quick Start

### Build & Run Locally

```bash
./mvnw spring-boot:run
# App available at http://localhost:8080/api/info
```

### Docker Build & Run

```bash
docker build -t anupam/k8s-deploy:latest .
docker run -p 8080:8080 anupam/k8s-deploy:latest
```

### Build with Jib (no Docker daemon needed)

```bash
./mvnw compile jib:dockerBuild
```

### Deploy to Kubernetes

```bash
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml
```

### Local Testing with Port Forward

```bash
kubectl port-forward svc/k8s-deploy-service 8080:80
curl http://localhost:8080/api/info
```

## Endpoints

| Path | Description |
|------|-------------|
| `/api/info` | App name, version, pod name, timestamp |
| `/actuator/health` | Health check |
| `/actuator/health/readiness` | Readiness probe |
| `/actuator/health/liveness` | Liveness probe |
