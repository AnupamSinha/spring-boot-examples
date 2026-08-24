package com.anupam.notification.producer;

import com.anupam.notification.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer that publishes notification requests to the configured topic.
 * <p>
 * Uses the channel name combined with the recipient as the Kafka message key
 * to ensure ordering guarantees for notifications to the same recipient.
 * </p>
 *
 * @author Anupam
 */
@Component
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;
    private final String topic;

    /**
     * Constructs the producer with the Kafka template and topic name.
     *
     * @param kafkaTemplate the Kafka template for sending messages
     * @param topic         the target Kafka topic name from configuration
     */
    public NotificationProducer(KafkaTemplate<String, NotificationRequest> kafkaTemplate,
                                @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publishes a notification request to the Kafka topic.
     * <p>
     * The message key is constructed as "CHANNEL-recipient" to ensure
     * partition affinity for the same recipient.
     * </p>
     *
     * @param request the notification request to publish
     */
    public void send(NotificationRequest request) {
        // Build a key that ensures same-recipient messages go to the same partition
        String key = request.channel().name() + "-" + request.recipient();
        log.info("Publishing notification to Kafka: channel={}, recipient={}, priority={}",
                request.channel(), request.recipient(), request.priority());

        kafkaTemplate.send(topic, key, request)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish notification: {}", ex.getMessage());
                    } else {
                        log.info("Notification published: offset={}",
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
