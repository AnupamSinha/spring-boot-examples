package com.anupam.ai.service;

import com.anupam.ai.model.ChatResponse;
import com.anupam.ai.tools.PaymentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Service that orchestrates the conversation between the user and the AI model.
 * Registers tools so the model can call them when needed.
 */
@Service
public class AssistantService {

    private final ChatClient chatClient;
    private final PaymentTools paymentTools;

    public AssistantService(ChatClient chatClient, PaymentTools paymentTools) {
        this.chatClient = chatClient;
        this.paymentTools = paymentTools;
    }

    /**
     * Sends a user message to the AI model with tool access.
     * The model may invoke one or more tools before producing a final answer.
     */
    public ChatResponse chat(String userMessage) {
        String answer = chatClient.prompt()
                .user(userMessage)
                .tools(paymentTools)
                .call()
                .content();

        return ChatResponse.of(answer);
    }
}
