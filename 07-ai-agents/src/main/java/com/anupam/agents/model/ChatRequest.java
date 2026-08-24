package com.anupam.agents.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        String message
) {}
