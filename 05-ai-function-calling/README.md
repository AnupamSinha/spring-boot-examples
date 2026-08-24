# Spring AI Function Calling — Tool Use Demo

A Spring Boot application demonstrating **Spring AI 2.0 tool calling** (function calling). The LLM invokes your Java methods at runtime to answer questions about live data.

> Blog post: [Spring AI Function Calling — Making LLMs Actually Do Things](https://anupamsinha.github.io/posts/spring-ai-function-calling-tool-use/)

---

## What This Does

The AI model can:

| Tool | Purpose |
|------|---------|
| `getPaymentStatus` | Look up a payment by transaction ID |
| `getRecentPayments` | List recent payments for a customer |
| `getExchangeRate` | Get live exchange rates between currencies |

The user asks questions in natural language. The model decides which tools to call, executes them, and composes a human-readable answer.

---

## Architecture

```
User Question
     │
     ▼
┌─────────────────┐
│   ChatClient    │──── sends message + tool schemas ────▶ OpenAI GPT-4o
└─────────────────┘                                            │
     ▲                                                         │
     │                                              tool call request
     │                                                         │
     │                                                         ▼
     │                                              ┌──────────────────┐
     └──────── tool result ◀────────────────────────│  PaymentTools    │
                                                    │  (your Java code)│
                                                    └──────────────────┘
```

---

## Prerequisites

- Java 21+
- Maven 3.9+
- OpenAI API key ([platform.openai.com](https://platform.openai.com))

---

## Quick Start

```bash
# 1. Clone the repo
git clone https://github.com/AnupamSinha/spring-ai-function.git
cd spring-ai-function

# 2. Set your OpenAI API key
export OPENAI_API_KEY=sk-your-key-here

# 3. Run
./mvnw spring-boot:run
```

---

## Try It

### Check a payment status

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the status of payment TXN-9042?"}'
```

### List recent payments

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Show me the last 3 payments for customer 42"}'
```

### Get exchange rate

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the current USD to INR exchange rate?"}'
```

### Ask something unrelated (no tool call)

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the capital of France?"}'
```

### Multiple tools in one query

```bash
curl -X POST http://localhost:8080/api/v1/assistant/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Show me recent payments for customer 42 and the current USD to EUR rate"}'
```

---

## Sample Data

The app comes with pre-loaded sample data:

| Transaction ID | Status | Amount | Sender | Receiver |
|---------------|--------|--------|--------|----------|
| TXN-9042 | COMPLETED | $250.00 | Alice Johnson | Bob Smith |
| TXN-9043 | PENDING | $1,200.50 | Charlie Brown | Diana Prince |
| TXN-9044 | FAILED | €75.00 | Eve Wilson | Frank Castle |
| TXN-9045 | COMPLETED | £500.00 | Grace Hopper | Alan Turing |
| TXN-9046 | COMPLETED | $89.99 | Alice Johnson | Eve Wilson |

Customers: `42` (3 payments), `101` (2 payments)

---

## Project Structure

```
src/main/java/com/anupam/ai/
├── SpringAiFunctionApplication.java    # Entry point
├── config/
│   └── AiConfig.java                   # ChatClient configuration + system prompt
├── controller/
│   └── AssistantController.java        # REST endpoint
├── model/
│   ├── ChatRequest.java                # Input DTO
│   ├── ChatResponse.java               # Output DTO
│   ├── ExchangeRate.java               # Tool return type
│   ├── PaymentInfo.java                # Tool return type
│   └── PaymentSummary.java             # Tool return type
├── service/
│   ├── AssistantService.java           # Orchestrates ChatClient + tools
│   ├── ExchangeRateService.java        # Simulated exchange rate data
│   └── PaymentService.java             # Simulated payment data
└── tools/
    └── PaymentTools.java               # @Tool annotated methods (the key file)
```

---

## Key Concepts

1. **`@Tool` annotation** — marks a method as callable by the AI model. The description tells the model *when* to use it.
2. **`@ToolParam` annotation** — describes each parameter so the model knows what to pass.
3. **Automatic JSON schema** — Spring AI generates the tool schema from your method signature. No manual OpenAPI spec needed.
4. **ToolCallingAdvisor** — handles the request/execute/feed-back loop automatically.
5. **Safety limits** — configured in `application.yml` to prevent runaway tool call loops.

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Spring AI | 2.0.0 |
| OpenAI model | GPT-4o |

---

## License

MIT
