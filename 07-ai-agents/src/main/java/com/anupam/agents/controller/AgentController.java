package com.anupam.agents.controller;

import com.anupam.agents.model.ChatRequest;
import com.anupam.agents.model.ChatResponse;
import com.anupam.agents.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST controller exposing both blocking and streaming agent endpoints.
 *
 * Demonstrates two consumption patterns for agentic AI:
 * - Blocking: complete response after all tool calls finish
 * - Streaming: tokens arrive via SSE as the model generates them
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * POST /api/v1/agent/plan - Blocking endpoint.
     * Waits for the full agent loop (multiple tool calls) to complete,
     * then returns the complete travel plan.
     */
    @PostMapping("/plan")
    public ResponseEntity<ChatResponse> plan(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = agentService.plan(request.message());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/agent/plan/stream - Streaming endpoint (SSE).
     * Returns tokens as Server-Sent Events as they're generated.
     * The tool calling loop still runs, but the final answer streams to the client.
     */
    @PostMapping(value = "/plan/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> planStream(@Valid @RequestBody ChatRequest request) {
        return agentService.planStream(request.message());
    }
}
