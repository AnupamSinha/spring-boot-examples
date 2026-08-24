package com.anupam.mcp.client.model;

import java.time.LocalDateTime;

public record ChatResponse(
        String answer,
        LocalDateTime timestamp
) {
    public static ChatResponse of(String answer) {
        return new ChatResponse(answer, LocalDateTime.now());
    }
}
