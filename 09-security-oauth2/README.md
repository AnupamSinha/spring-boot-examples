# Spring Security 6 + OAuth2/OIDC — Resource Server with Keycloak

A Spring Boot resource server secured with OAuth2 JWT tokens from Keycloak.

> Blog post: [Spring Security 6 + OAuth2/OIDC — From Login to Resource Server](https://anupamsinha.github.io/posts/spring-security-oauth2-oidc-keycloak/)

## Quick Start

```bash
# 1. Start Keycloak
docker compose up -d

# 2. Configure Keycloak (http://localhost:8180, admin/admin)
#    - Create realm: payment-realm
#    - Create client: payment-api (Client auth ON)
#    - Create roles: ADMIN, USER
#    - Create users: alice (ADMIN), bob (USER)

# 3. Run the app
./mvnw spring-boot:run

# 4. Get a token
TOKEN=$(curl -s -X POST http://localhost:8180/realms/payment-realm/protocol/openid-connect/token \
  -d "client_id=payment-api" \
  -d "client_secret=YOUR_SECRET" \
  -d "username=alice" \
  -d "password=alice123" \
  -d "grant_type=password" | jq -r '.access_token')

# 5. Call the API
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/payments
```

## Endpoints

| Endpoint | Access |
|----------|--------|
| `GET /api/public/health` | Public (no auth) |
| `GET /api/payments` | USER or ADMIN |
| `GET /api/admin/users` | ADMIN only |
| `GET /api/me` | Any authenticated user |

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Spring Security | 6.x |
| Keycloak | 25.0 |

## License

MIT
