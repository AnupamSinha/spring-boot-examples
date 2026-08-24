package com.anupam.websocket.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public Map<String, String> sendNotification(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "No message");

        Map<String, Object> notification = Map.of(
                "message", message,
                "timestamp", Instant.now().toString(),
                "type", "SERVER_NOTIFICATION"
        );

        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return Map.of("status", "sent", "message", message);
    }
}
