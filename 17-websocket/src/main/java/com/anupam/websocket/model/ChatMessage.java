package com.anupam.websocket.model;

import java.time.Instant;

/**
 * Immutable record representing a chat message exchanged over WebSocket.
 * <p>
 * Supports three message types: CHAT (regular messages), JOIN (user joined),
 * and LEAVE (user left). Provides factory methods for creating typed messages.
 * </p>
 *
 * @param sender    the username of the message sender
 * @param content   the text content of the message
 * @param timestamp the instant when the message was created
 * @param type      the message type (CHAT, JOIN, or LEAVE)
 *
 * @author Anupam
 */
public record ChatMessage(
        String sender,
        String content,
        Instant timestamp,
        MessageType type
) {
    /**
     * Enumeration of supported chat message types.
     */
    public enum MessageType {
        /** A regular chat message. */
        CHAT,
        /** A notification that a user has joined. */
        JOIN,
        /** A notification that a user has left. */
        LEAVE
    }

    /**
     * Creates a CHAT-type message with the current timestamp.
     *
     * @param sender  the username of the sender
     * @param content the message content
     * @return a new chat message of type CHAT
     */
    public static ChatMessage chat(String sender, String content) {
        return new ChatMessage(sender, content, Instant.now(), MessageType.CHAT);
    }

    /**
     * Creates a JOIN-type message indicating that a user has entered the chat.
     *
     * @param sender the username of the joining user
     * @return a new chat message of type JOIN
     */
    public static ChatMessage join(String sender) {
        return new ChatMessage(sender, sender + " joined the chat", Instant.now(), MessageType.JOIN);
    }

    /**
     * Creates a LEAVE-type message indicating that a user has left the chat.
     *
     * @param sender the username of the leaving user
     * @return a new chat message of type LEAVE
     */
    public static ChatMessage leave(String sender) {
        return new ChatMessage(sender, sender + " left the chat", Instant.now(), MessageType.LEAVE);
    }
}
