package com.anupam.websocket.controller;

import com.anupam.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

/**
 * WebSocket controller handling real-time chat operations.
 * <p>
 * Manages chat messages, user join events, and user leave events,
 * broadcasting them to all subscribers of the "/topic/messages" destination.
 * </p>
 *
 * @author Anupam
 */
@Controller
public class ChatController {

    /**
     * Processes an incoming chat message and broadcasts it to all subscribers.
     * <p>
     * Adds a server-side timestamp before forwarding the message.
     * </p>
     *
     * @param message the chat message received from the client
     * @return the message with an updated timestamp, broadcast to "/topic/messages"
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Attach server-generated timestamp for consistency
        return new ChatMessage(
                message.sender(),
                message.content(),
                Instant.now(),
                message.type()
        );
    }

    /**
     * Handles a user joining the chat room.
     * <p>
     * Creates a JOIN-type message and broadcasts it to notify all participants.
     * </p>
     *
     * @param message the join message containing the sender's name
     * @return a JOIN-type chat message broadcast to "/topic/messages"
     */
    @MessageMapping("/chat.join")
    @SendTo("/topic/messages")
    public ChatMessage join(ChatMessage message) {
        return ChatMessage.join(message.sender());
    }

    /**
     * Handles a user leaving the chat room.
     * <p>
     * Creates a LEAVE-type message and broadcasts it to notify all participants.
     * </p>
     *
     * @param message the leave message containing the sender's name
     * @return a LEAVE-type chat message broadcast to "/topic/messages"
     */
    @MessageMapping("/chat.leave")
    @SendTo("/topic/messages")
    public ChatMessage leave(ChatMessage message) {
        return ChatMessage.leave(message.sender());
    }
}
