package com.anupam.agents.model;

import java.time.LocalDateTime;

/**
 * Response from the travel planning agent.
 *
 * @param answer    the AI-generated travel plan or response
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
