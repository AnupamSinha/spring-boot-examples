package com.anupam.mcp.client.model;

import java.time.LocalDateTime;

/**
 * Response returned to the caller after AI processing.
 *
 * @param answer    the AI-generated response text
 * @param timestamp when the response was generated
 * @author Anupam
 */
public record ChatResponse(
        String answer,
        LocalDateTime timestamp
) {
    /** Factory method that auto-sets the timestamp to now. */
    public static ChatResponse of(String answer) {
        return new ChatResponse(answer, LocalDateTime.now());
    }
}
