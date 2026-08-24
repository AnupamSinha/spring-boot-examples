# Spring AI MCP — Server & Client Demo

A multi-module Spring Boot project demonstrating the **Model Context Protocol (MCP)** with Spring AI 2.0. One application exposes tools as an MCP server; another consumes them as an MCP client with an AI model.

> Blog post: [Spring AI + MCP — Exposing Tools as a Standardized Server](https://anupamsinha.github.io/posts/spring-ai-mcp-server-client/)

---

## Architecture

```
┌────────────────────────┐          MCP (SSE)          ┌────────────────────────┐
│      MCP Client        │ ◀──── tool discovery ─────▶ │      MCP Server        │
│   (port 8080)          │                             │   (port 8081)          │
│                        │                             │                        │
│  ChatClient + OpenAI   │ ──── tool call request ───▶ │  @Tool methods         │
│                        │ ◀──── tool result ───────── │  (payment, exchange)   │
│  REST API for users    │                             │                        │
└────────────────────────┘                             └────────────────────────┘
```

The **MCP Server** knows nothing about AI models — it just exposes tools. The **MCP Client** connects to the server, discovers available tools, and passes them to the LLM for use.

---

## Modules

| Module | Port | Purpose |
|--------|------|---------|
| `mcp-server` | 8081 | Exposes payment tools via MCP (SSE transport) |
| `mcp-client` | 8080 | AI assistant that discovers + uses tools from the MCP server |

---

## Prerequisites

- Java 21+
- Maven 3.9+
- OpenAI API key

---

## Quick Start

### 1. Start the MCP Server

```bash
cd mcp-server
../mvnw spring-boot:run
```

The server starts on port 8081 and exposes these tools via MCP:
- `getPaymentStatus` — look up a payment by transaction ID
- `getExchangeRate` — get exchange rate between currencies
- `convertPaymentAmount` — convert a payment amount to another currency

### 2. Start the MCP Client

```bash
cd mcp-client
export OPENAI_API_KEY=sk-your-key-here
../mvnw spring-boot:run
```

The client connects to the MCP server, discovers available tools, and registers them with the ChatClient.

### 3. Test

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the status of payment TXN-9042?"}'
```

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Convert TXN-9043 amount to EUR"}'
```

---

## Project Structure

```
spring-ai-mcp/
├── pom.xml                          # Parent POM (multi-module)
├── mcp-server/
│   ├── pom.xml
│   └── src/main/java/com/anupam/mcp/server/
│       ├── McpServerApplication.java
│       ├── model/
│       │   ├── PaymentInfo.java
│       │   └── ExchangeRate.java
│       └── tools/
│           └── PaymentMcpTools.java  # @Tool methods exposed via MCP
└── mcp-client/
    ├── pom.xml
    └── src/main/java/com/anupam/mcp/client/
        ├── McpClientApplication.java
        ├── config/
        │   └── AiConfig.java         # Wires MCP tools into ChatClient
        ├── controller/
        │   └── AssistantController.java
        └── model/
            ├── ChatRequest.java
            └── ChatResponse.java
```

---

## Key Concepts

1. **MCP Server** — annotate methods with `@Tool` and add `spring-ai-starter-mcp-server-webmvc`. Auto-configuration handles registration and SSE transport.
2. **MCP Client** — add `spring-ai-starter-mcp-client` and configure the server URL. `SyncMcpToolCallbackProvider` is auto-created.
3. **Tool Discovery** — the client discovers tools at startup. No need to define tool schemas on the client side.
4. **Transport** — uses Server-Sent Events (SSE) over HTTP. Also supports stdio for local processes.

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Spring AI | 2.0.0 |
| OpenAI | GPT-4o |

---

## License

MIT
