package com.anupam.websocket.controller;

import com.anupam.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return new ChatMessage(
                message.sender(),
                message.content(),
                Instant.now(),
                message.type()
        );
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/messages")
    public ChatMessage join(ChatMessage message) {
        return ChatMessage.join(message.sender());
    }

    @MessageMapping("/chat.leave")
    @SendTo("/topic/messages")
    public ChatMessage leave(ChatMessage message) {
        return ChatMessage.leave(message.sender());
    }
}
