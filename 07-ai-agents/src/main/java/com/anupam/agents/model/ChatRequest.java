package com.anupam.agents.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming chat request for the travel planning agent.
 *
 * @param message the user's travel query (e.g., "Plan a trip to Paris for 5 days")
 * @author Anupam
 */
public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        String message
) {}
