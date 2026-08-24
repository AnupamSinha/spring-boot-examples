# Spring AI Agentic Patterns — Multi-Step Tool Chains with Streaming

A Spring Boot application demonstrating **agentic AI patterns** with Spring AI 2.0. The model autonomously plans and executes multi-step tool chains to build a complete travel plan — with both blocking and streaming response modes.

> Blog post: [Spring AI Agentic Patterns — When the Model Plans the Workflow](https://anupamsinha.github.io/posts/spring-ai-agentic-patterns-streaming/)

---

## What This Does

A single user prompt like *"Plan a 5-day trip to Paris"* triggers the model to autonomously:

1. Check the weather forecast
2. Search for flights
3. Search for hotels
4. Get recommended activities
5. Calculate the total budget

The model decides the order, the arguments, and when to stop — no hardcoded workflow.

---

## Two Response Modes

| Endpoint | Mode | Use Case |
|----------|------|----------|
| `POST /api/v1/agent/plan` | Blocking | API-to-API, batch jobs |
| `POST /api/v1/agent/plan/stream` | Streaming (SSE) | Real-time UIs, chatbots |

---

## Prerequisites

- Java 21+
- Maven 3.9+
- OpenAI API key

---

## Quick Start

```bash
git clone https://github.com/AnupamSinha/spring-ai-agents.git
cd spring-ai-agents
export OPENAI_API_KEY=sk-your-key-here
./mvnw spring-boot:run
```

---

## Try It

### Blocking (complete response)

```bash
curl -X POST http://localhost:8080/api/v1/agent/plan \
  -H "Content-Type: application/json" \
  -d '{"message": "Plan a 5-day trip to Paris from New York. Mid-range budget."}'
```

### Streaming (token-by-token via SSE)

```bash
curl -X POST http://localhost:8080/api/v1/agent/plan/stream \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{"message": "Plan a weekend trip to Tokyo. I like food and culture."}'
```

### Different destinations

```bash
# London
curl -X POST http://localhost:8080/api/v1/agent/plan \
  -H "Content-Type: application/json" \
  -d '{"message": "Plan 3 days in London for a history enthusiast."}'

# Barcelona
curl -X POST http://localhost:8080/api/v1/agent/plan \
  -H "Content-Type: application/json" \
  -d '{"message": "Beach trip to Barcelona, 4 nights, budget-friendly."}'
```

---

## The Agentic Pattern

```
User: "Plan a 5-day trip to Paris"
           │
           ▼
┌─────────────────────────────────────────────────┐
│  Model decides: I need weather first            │
│  → calls getWeatherForecast("Paris")            │
│  Result: "Partly cloudy, 22°C, perfect..."      │
│                                                 │
│  Model decides: Now I need flights              │
│  → calls searchFlights("New York", "Paris"...)  │
│  Result: [3 flight options]                     │
│                                                 │
│  Model decides: Now hotels                      │
│  → calls searchHotels("Paris", "2026-08-25", 5) │
│  Result: [4 hotel options]                      │
│                                                 │
│  Model decides: Activities next                 │
│  → calls getActivities("Paris", "culture")      │
│  Result: [5 activity suggestions]              │
│                                                 │
│  Model decides: Calculate budget                │
│  → calls calculateBudget(450, 195, 5, 80, 50)  │
│  Result: "TOTAL: $2,550.00"                    │
│                                                 │
│  Model decides: All data collected → compose    │
│  → generates final structured travel plan       │
└─────────────────────────────────────────────────┘
           │
           ▼
    Complete Travel Plan
```

The key insight: **you define the tools, the model decides the workflow.** No orchestration code needed.

---

## Project Structure

```
src/main/java/com/anupam/agents/
├── SpringAiAgentsApplication.java
├── config/
│   └── AiConfig.java              # System prompt defines agent behavior
├── controller/
│   └── AgentController.java       # Blocking + streaming endpoints
├── model/
│   ├── ChatRequest.java
│   ├── ChatResponse.java
│   ├── FlightOption.java
│   ├── HotelOption.java
│   └── TravelPlan.java
├── service/
│   └── AgentService.java          # Blocking .call() and streaming .stream()
└── tools/
    └── TravelTools.java           # 5 tools the agent can chain
```

---

## Key Concepts

1. **Agentic Loop** — `ToolCallingAdvisor` drives the loop: model → tool call → execute → model → ... until no more tools needed.
2. **Autonomous Planning** — the system prompt guides behavior, but the model decides *which* tools to call and in *what* order.
3. **Streaming with Tools** — `.stream().content()` returns a `Flux<String>`. Tool calls still execute during the loop; the final response streams.
4. **Tool Call Limits** — safety net to prevent infinite loops (`max-total-tool-calls: 40`).
5. **System Prompt as Orchestrator** — the prompt defines the agent's strategy without hardcoding a workflow.

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.5.0 |
| Spring AI | 2.0.0 |
| OpenAI | GPT-4o |
| WebFlux | For streaming SSE responses |

---

## License

MIT
