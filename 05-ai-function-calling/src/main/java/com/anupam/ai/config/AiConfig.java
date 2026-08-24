package com.anupam.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the ChatClient with a system prompt that defines
 * the assistant's personality and behavior boundaries.
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a helpful payments assistant. You can:
                        - Look up payment status by transaction ID
                        - Show recent payments for a customer
                        - Provide current exchange rates between currencies

                        Rules:
                        - Always confirm the transaction ID or customer ID before taking action.
                        - If a tool returns no results or an error, tell the user clearly.
                        - Do not make up payment data. Only report what the tools return.
                        - For exchange rates, always mention the timestamp so users know it's current.
                        """)
                .build();
    }
}
