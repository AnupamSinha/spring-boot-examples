package com.anupam.notification.channel;

import com.anupam.notification.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Push notification channel implementation (simulated).
 * <p>
 * In production, this would integrate with Firebase Cloud Messaging (FCM)
 * or Apple Push Notification service (APNs).
 * </p>
 *
 * @author Anupam
 */
@Component
public class PushChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(PushChannel.class);

    @Override
    public NotificationRequest.Channel getChannel() {
        return NotificationRequest.Channel.PUSH;
    }

    /**
     * Sends a push notification to the device (simulated).
     * <p>
     * In production, this would call the FCM or APNs API with the
     * device token from the recipient field.
     * </p>
     *
     * @param request the notification request containing the device token and template
     */
    @Override
    public void send(NotificationRequest request) {
        // In production: call FCM API with the device token and notification payload
        log.info("[PUSH] Sending to device token {}: template={}",
                request.recipient(), request.templateName());
        log.info("[PUSH] Delivered successfully to: {}", request.recipient());
    }
}
