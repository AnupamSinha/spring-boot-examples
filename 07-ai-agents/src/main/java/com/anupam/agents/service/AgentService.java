package com.anupam.agents.service;

import com.anupam.agents.model.ChatResponse;
import com.anupam.agents.tools.TravelTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service demonstrating two agentic patterns:
 *
 * 1. Blocking (call) — the model plans and executes all tool calls,
 *    returning a complete answer when done. Good for background jobs.
 *
 * 2. Streaming (stream) — tokens stream to the client as the model
 *    generates them, including during tool call loops. Good for UIs.
 */
@Service
public class AgentService {

    private final ChatClient chatClient;
    private final TravelTools travelTools;

    public AgentService(ChatClient chatClient, TravelTools travelTools) {
        this.chatClient = chatClient;
        this.travelTools = travelTools;
    }

    /**
     * Blocking agent: model calls tools in sequence, returns final answer.
     * The tool calling loop runs to completion before returning.
     */
    public ChatResponse plan(String userMessage) {
        String answer = chatClient.prompt()
                .user(userMessage)
                .tools(travelTools)
                .call()
                .content();

        return ChatResponse.of(answer);
    }

    /**
     * Streaming agent: tokens arrive as they're generated.
     * The model still calls tools (blocking during the loop), but the
     * final response streams token-by-token to the client via SSE.
     */
    public Flux<String> planStream(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .tools(travelTools)
                .stream()
                .content();
    }
}
