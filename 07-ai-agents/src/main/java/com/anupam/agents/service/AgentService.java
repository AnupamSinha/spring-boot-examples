package com.anupam.agents.service;

import com.anupam.agents.model.ChatResponse;
import com.anupam.agents.tools.TravelTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service demonstrating two agentic execution patterns.
 *
 * <ul>
 *   <li><b>Blocking (call)</b> - The model plans and executes all tool calls,
 *       returning a complete answer when done. Good for background jobs and APIs.</li>
 *   <li><b>Streaming (stream)</b> - Tokens stream to the client as the model
 *       generates them, including during tool call loops. Good for chat UIs.</li>
 * </ul>
 *
 * @author Anupam
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
     * Blocking agent: the model calls tools in sequence and returns
     * the final answer once the full tool-calling loop completes.
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
     * Streaming agent: tokens arrive as they're generated via Reactor Flux.
     * Tool calls still execute synchronously, but the final text streams token-by-token.
     */
    public Flux<String> planStream(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .tools(travelTools)
                .stream()
                .content();
    }
}
