package com.anupam.mcp.client.controller;

import com.anupam.mcp.client.model.ChatRequest;
import com.anupam.mcp.client.model.ChatResponse;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the MCP-powered AI assistant.
 *
 * Tools are discovered automatically from the MCP server at startup.
 * No local @Tool definitions are needed - the model invokes remote tools
 * via the Model Context Protocol transport (stdio or SSE).
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final ChatClient chatClient;

    public AssistantController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * POST /api/v1/assistant/chat - Sends a user message to the AI.
     * The model may invoke MCP tools before producing its response.
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

        return ResponseEntity.ok(ChatResponse.of(answer));
    }
}
