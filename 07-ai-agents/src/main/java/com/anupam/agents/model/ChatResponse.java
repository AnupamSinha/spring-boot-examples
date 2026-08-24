package com.anupam.agents.model;

import java.time.LocalDateTime;

public record ChatResponse(
        String answer,
        LocalDateTime timestamp
) {
    public static ChatResponse of(String answer) {
        return new ChatResponse(answer, LocalDateTime.now());
    }
}
