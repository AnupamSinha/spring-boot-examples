package com.anupam.websocket.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * REST controller for sending server-initiated notifications via WebSocket.
 * <p>
 * Exposes an HTTP POST endpoint that allows external systems to push
 * notifications to WebSocket-subscribed clients on the "/topic/notifications" channel.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs the controller with the required messaging template.
     *
     * @param messagingTemplate the STOMP messaging template for sending messages to clients
     */
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Receives a notification payload via HTTP POST and broadcasts it to WebSocket subscribers.
     * <p>
     * The payload is enriched with a timestamp and type before being sent to the
     * "/topic/notifications" destination.
     * </p>
     *
     * @param payload a map containing a "message" key with the notification text
     * @return a confirmation map with "status" and "message" keys
     */
    @PostMapping
    public Map<String, String> sendNotification(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "No message");

        // Build the notification with metadata
        Map<String, Object> notification = Map.of(
                "message", message,
                "timestamp", Instant.now().toString(),
                "type", "SERVER_NOTIFICATION"
        );

        // Broadcast to all WebSocket subscribers on the notifications topic
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return Map.of("status", "sent", "message", message);
    }
}
