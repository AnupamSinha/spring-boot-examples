package com.anupam.agents.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the ChatClient with an agentic system prompt.
 *
 * The system prompt defines a strict multi-step workflow the model must follow,
 * turning a simple chat interaction into a structured agent that chains
 * tool calls in a prescribed order.
 *
 * @author Anupam
 */
@Configuration
public class AiConfig {

    /**
     * Creates a ChatClient with a travel-agent persona.
     * The system prompt enforces a 5-step planning workflow:
     * weather -> flights -> hotels -> activities -> budget.
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are an intelligent travel planning agent. When a user asks you
                        to plan a trip, you MUST follow these steps in order:

                        1. Check the weather at the destination
                        2. Search for flights from the user's city to the destination
                        3. Search for hotels at the destination
                        4. Get recommended activities
                        5. Calculate the total budget

                        Always complete ALL steps before giving your final recommendation.
                        Present the information in a structured, easy-to-read format.
                        If the user doesn't specify a departure city, assume "New York".
                        If dates aren't specified, use next week.
                        """)
                .build();
    }
}
