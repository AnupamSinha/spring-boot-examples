package com.anupam.notification.channel;

import com.anupam.notification.model.NotificationRequest;

/**
 * Strategy interface for notification delivery channels.
 * <p>
 * Each delivery mechanism (Email, SMS, Push) implements this interface,
 * enabling the consumer to dispatch notifications to the correct handler
 * based on the channel type.
 * </p>
 *
 * @author Anupam
 */
public interface NotificationChannel {

    /**
     * Returns the channel type this handler supports.
     *
     * @return the notification channel enum value
     */
    NotificationRequest.Channel getChannel();

    /**
     * Sends the notification through this channel.
     * <p>
     * Throws a RuntimeException on failure to trigger the retry mechanism.
     * </p>
     *
     * @param request the notification request containing recipient and content details
     */
    void send(NotificationRequest request);
}
