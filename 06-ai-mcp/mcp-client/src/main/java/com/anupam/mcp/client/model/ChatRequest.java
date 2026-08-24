package com.anupam.mcp.client.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming chat request containing the user's message.
 *
 * @param message the user's question or instruction (must not be blank)
 * @author Anupam
 */
public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        String message
) {}
