package com.anupam.notification.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Request DTO representing a notification to be sent through a specific channel.
 * <p>
 * Carries the recipient, delivery channel, template name for content rendering,
 * variable substitutions, and priority level.
 * </p>
 *
 * @param recipient    the target address (email, phone number, or device token)
 * @param channel      the delivery channel (EMAIL, SMS, or PUSH)
 * @param templateName the name of the content template to render
 * @param variables    key-value pairs for template variable substitution
 * @param priority     the delivery priority level
 * @author Anupam
 */
public record NotificationRequest(
        @NotBlank(message = "Recipient must not be blank")
        String recipient,

        @NotNull(message = "Channel must not be null")
        Channel channel,

        @NotBlank(message = "Template name must not be blank")
        String templateName,

        Map<String, String> variables,

        Priority priority
) {
    /**
     * Supported notification delivery channels.
     */
    public enum Channel {
        EMAIL, SMS, PUSH
    }

    /**
     * Priority levels for notification delivery ordering.
     */
    public enum Priority {
        HIGH, MEDIUM, LOW
    }
}
