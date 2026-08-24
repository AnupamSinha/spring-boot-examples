package com.anupam.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration that sets up STOMP messaging over SockJS.
 * <p>
 * Configures the message broker with a simple in-memory broker for topic-based
 * subscriptions and defines the application destination prefix for client messages.
 * </p>
 *
 * @author Anupam
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for broadcasting messages to subscribed clients.
     * <p>
     * Enables a simple broker on the "/topic" prefix and sets "/app" as the
     * application-level destination prefix for messages from clients.
     * </p>
     *
     * @param config the message broker registry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker for "/topic" destinations
        config.enableSimpleBroker("/topic");
        // Messages from clients prefixed with "/app" are routed to @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints that clients connect to for WebSocket communication.
     * <p>
     * Exposes a "/ws" endpoint with SockJS fallback support and allows all origins.
     * </p>
     *
     * @param registry the STOMP endpoint registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // Allow connections from any origin
                .setAllowedOriginPatterns("*")
                // Enable SockJS fallback for browsers that don't support WebSocket
                .withSockJS();
    }
}
