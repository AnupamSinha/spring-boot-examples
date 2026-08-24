# Feature Flags with Spring Boot + Togglz

A demonstration of feature flags using Togglz in Spring Boot 3.5. Decouple deployment from release with runtime-toggleable features.

## Features

- **NEW_CHECKOUT** — New checkout flow (gradual rollout)
- **DARK_MODE** — Dark mode UI toggle (user-based)
- **PREMIUM_SEARCH** — Enhanced search for premium users

## Running

```bash
./mvnw spring-boot:run
```

## Endpoints

```bash
# Product listing (behavior changes based on PREMIUM_SEARCH flag)
curl http://localhost:8080/api/products?query=laptop

# Check all feature flag states
curl http://localhost:8080/api/features

# Toggle a feature ON
curl -X PUT http://localhost:8080/api/features/NEW_CHECKOUT/enable

# Toggle a feature OFF
curl -X PUT http://localhost:8080/api/features/NEW_CHECKOUT/disable
```

## Togglz Admin Console

Visit: [http://localhost:8080/togglz-console](http://localhost:8080/togglz-console)

## Blog Post

Full explanation: [Feature Flags with Spring Boot](https://anupamsinha.github.io/posts/spring-boot-feature-flags/)
