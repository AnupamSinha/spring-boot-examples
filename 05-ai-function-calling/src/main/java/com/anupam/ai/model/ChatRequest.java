package com.anupam.ai.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming chat request from the user.
 */
public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        String message
) {}
