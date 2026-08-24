package com.anupam.ai.controller;

import com.anupam.ai.model.ChatRequest;
import com.anupam.ai.model.ChatResponse;
import com.anupam.ai.service.AssistantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the AI-powered payment assistant.
 */
@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    /**
     * Chat endpoint. The AI model may invoke tools (check payment status,
     * list recent payments, get exchange rates) to answer the user's question.
     *
     * <p>Example requests:
     * <ul>
     *   <li>"What's the status of payment TXN-9042?"</li>
     *   <li>"Show me the last 3 payments for customer 42"</li>
     *   <li>"What's the current USD to EUR exchange rate?"</li>
     *   <li>"What's the capital of France?" (no tool needed)</li>
     * </ul>
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = assistantService.chat(request.message());
        return ResponseEntity.ok(response);
    }
}
