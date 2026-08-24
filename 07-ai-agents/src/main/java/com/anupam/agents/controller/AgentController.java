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
 * Controller exposing both blocking and streaming agent endpoints.
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * Blocking endpoint: waits for the full agent loop (multiple tool calls)
     * to complete, then returns the complete travel plan.
     *
     * Use when: background processing, API-to-API calls, batch jobs.
     */
    @PostMapping("/plan")
    public ResponseEntity<ChatResponse> plan(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = agentService.plan(request.message());
        return ResponseEntity.ok(response);
    }

    /**
     * Streaming endpoint: returns tokens as Server-Sent Events as they're generated.
     * The tool calling loop still runs, but the final answer streams to the client.
     *
     * Use when: real-time UIs, chatbots, anywhere users expect progressive output.
     */
    @PostMapping(value = "/plan/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> planStream(@Valid @RequestBody ChatRequest request) {
        return agentService.planStream(request.message());
    }
}
