package com.anupam.websocket.model;

import java.time.Instant;

public record ChatMessage(
        String sender,
        String content,
        Instant timestamp,
        MessageType type
) {
    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    public static ChatMessage chat(String sender, String content) {
        return new ChatMessage(sender, content, Instant.now(), MessageType.CHAT);
    }

    public static ChatMessage join(String sender) {
        return new ChatMessage(sender, sender + " joined the chat", Instant.now(), MessageType.JOIN);
    }

    public static ChatMessage leave(String sender) {
        return new ChatMessage(sender, sender + " left the chat", Instant.now(), MessageType.LEAVE);
    }
}
