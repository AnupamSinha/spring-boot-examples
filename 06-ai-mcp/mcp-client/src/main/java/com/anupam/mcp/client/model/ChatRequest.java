package com.anupam.mcp.client.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        String message
) {}
