package com.anupam.ai.model;

import java.time.LocalDateTime;

/**
 * Response returned to the user after the AI processes the question.
 */
public record ChatResponse(
        String answer,
        LocalDateTime timestamp
) {
    public static ChatResponse of(String answer) {
        return new ChatResponse(answer, LocalDateTime.now());
    }
}
