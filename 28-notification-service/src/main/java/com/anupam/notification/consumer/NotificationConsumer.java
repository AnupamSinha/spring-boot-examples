package com.anupam.notification.consumer;

import com.anupam.notification.channel.NotificationChannel;
import com.anupam.notification.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Kafka consumer that processes notification messages and dispatches
 * them to the appropriate delivery channel.
 * <p>
 * Supports automatic retries with exponential backoff and routes
 * permanently failed messages to a dead letter topic (DLT).
 * </p>
 *
 * @author Anupam
 */
@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final Map<NotificationRequest.Channel, NotificationChannel> channelHandlers;

    /**
     * Constructs the consumer by indexing all available channel handlers
     * by their supported channel type for O(1) lookup during dispatch.
     *
     * @param channels the list of all registered notification channel implementations
     */
    public NotificationConsumer(List<NotificationChannel> channels) {
        this.channelHandlers = channels.stream()
                .collect(Collectors.toMap(NotificationChannel::getChannel, Function.identity()));
    }

    /**
     * Consumes notification messages from Kafka and dispatches to the
     * appropriate channel handler.
     * <p>
     * Configured with retryable topic support: up to 3 attempts with
     * exponential backoff (1s, 2s) before routing to the dead letter topic.
     * </p>
     *
     * @param request the notification request deserialized from Kafka
     * @throws IllegalArgumentException if no handler exists for the requested channel
     */
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "notification-group")
    public void consume(NotificationRequest request) {
        log.info("Received notification: channel={}, recipient={}, template={}",
                request.channel(), request.recipient(), request.templateName());

        // Look up the handler for the requested channel
        NotificationChannel handler = channelHandlers.get(request.channel());
        if (handler == null) {
            log.error("No handler found for channel: {}", request.channel());
            throw new IllegalArgumentException("Unsupported channel: " + request.channel());
        }

        // Delegate delivery to the channel-specific handler
        handler.send(request);
        log.info("Notification sent successfully: channel={}, recipient={}",
                request.channel(), request.recipient());
    }

    /**
     * Dead letter topic handler invoked when all retry attempts are exhausted.
     * <p>
     * In production, this would persist the failed notification to a database
     * and alert the operations team for manual intervention.
     * </p>
     *
     * @param request the notification request that could not be delivered
     */
    @DltHandler
    public void handleDlt(NotificationRequest request) {
        log.error("Notification moved to DLT after retries exhausted: channel={}, recipient={}",
                request.channel(), request.recipient());
        // In production: persist to DB, alert ops team, etc.
    }
}
