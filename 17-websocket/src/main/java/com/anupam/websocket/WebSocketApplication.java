package com.anupam.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the WebSocket demonstration application.
 * <p>
 * This application showcases real-time communication using STOMP over WebSocket,
 * including a chat system and server-sent notifications.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class WebSocketApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
    }
}
