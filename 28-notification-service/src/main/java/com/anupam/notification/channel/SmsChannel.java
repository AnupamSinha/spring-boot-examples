package com.anupam.notification.channel;

import com.anupam.notification.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * SMS notification channel implementation (simulated).
 * <p>
 * In production, this would integrate with an SMS gateway such as
 * Twilio, AWS SNS, or a similar provider.
 * </p>
 *
 * @author Anupam
 */
@Component
public class SmsChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(SmsChannel.class);

    @Override
    public NotificationRequest.Channel getChannel() {
        return NotificationRequest.Channel.SMS;
    }

    /**
     * Sends an SMS notification to the recipient (simulated).
     *
     * @param request the notification request containing the phone number and content
     */
    @Override
    public void send(NotificationRequest request) {
        // In production: call Twilio or AWS SNS API to send the SMS
        String messageBody = buildMessageBody(request);
        log.info("[SMS] Sending to {}: {}", request.recipient(), messageBody);
        log.info("[SMS] Delivered successfully to: {}", request.recipient());
    }

    /**
     * Builds the SMS message body from the request variables.
     * <p>
     * If a "code" variable is present, formats it as a verification code message.
     * Otherwise, returns a generic notification message.
     * </p>
     *
     * @param request the notification request
     * @return the formatted SMS message body
     */
    private String buildMessageBody(NotificationRequest request) {
        if (request.variables() != null && request.variables().containsKey("code")) {
            return "Your verification code is: " + request.variables().get("code");
        }
        return "You have a new notification from our platform.";
    }
}
