package com.anupam.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the ChatClient to use tools discovered from MCP servers.
 *
 * The SyncMcpToolCallbackProvider is auto-configured by Spring AI and connects
 * to all MCP servers defined in application.yml. Tools are dynamically
 * discovered at startup and made available to the LLM without local definitions.
 *
 * @author Anupam
 */
@Configuration
public class AiConfig {

    /**
     * Creates a ChatClient wired with remotely-discovered MCP tools.
     * The system prompt constrains the assistant to payment-related queries.
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider mcpTools) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a helpful payments assistant. You have access to tools
                        provided by an MCP server. Use them to look up payment status,
                        get exchange rates, and convert payment amounts.
                        If a tool returns an error, relay it clearly to the user.
                        """)
                .defaultTools(mcpTools)
                .build();
    }
}
