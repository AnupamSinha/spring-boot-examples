# Spring Boot WebSocket — Real-Time Notifications and Chat

A Spring Boot application demonstrating real-time bidirectional communication using WebSocket and STOMP protocol.

## Features

- **STOMP over WebSocket** — structured messaging with topics and subscriptions
- **Chat application** — real-time message broadcasting
- **Server-push notifications** — push events from REST endpoints to connected clients
- **SockJS fallback** — graceful degradation for older browsers

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring WebSocket + STOMP
- SockJS + STOMP.js (frontend)

## Getting Started

```bash
# Run the application
./mvnw spring-boot:run

# Open the chat UI
open http://localhost:8080

# Send a server notification (from another terminal)
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Server maintenance in 5 minutes"}'
```

## Blog Post

Full walkthrough: [Spring Boot + WebSocket — Real-Time Notifications and Chat](https://anupamsinha.github.io/posts/spring-boot-websocket-real-time/)
