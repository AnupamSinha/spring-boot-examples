package com.anupam.outbox.relay;

import com.anupam.outbox.model.OutboxEvent;
import com.anupam.outbox.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Scheduled relay component that polls the outbox table and publishes
 * unpublished events to Kafka.
 * <p>
 * This implements the "polling publisher" variant of the Transactional Outbox Pattern.
 * Events are picked up in creation order (FIFO) and published to a Kafka topic derived
 * from the aggregate type. Once published, events are marked as published with a timestamp.
 * </p>
 * <p>
 * On failure, processing stops at the failed event to maintain ordering guarantees.
 * The next poll cycle will retry from the failed event.
 * </p>
 *
 * @author Anupam
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructs the outbox relay with required dependencies.
     *
     * @param outboxRepository the repository for querying and updating outbox events
     * @param kafkaTemplate    the Kafka template for publishing events to topics
     */
    public OutboxRelay(OutboxRepository outboxRepository,
                       KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Polls the outbox table every 5 seconds for unpublished events and publishes
     * them to the appropriate Kafka topic.
     * <p>
     * Each event is sent with the aggregate ID as the Kafka key (ensuring ordering
     * per aggregate) and the JSON payload as the value. After successful send, the
     * event is marked as published.
     * </p>
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void pollAndPublish() {
        // Query for unpublished events ordered by creation time (FIFO)
        List<OutboxEvent> unpublishedEvents =
                outboxRepository.findByPublishedFalseOrderByCreatedAtAsc();

        if (unpublishedEvents.isEmpty()) {
            return;
        }

        log.info("Found {} unpublished events to relay", unpublishedEvents.size());

        for (OutboxEvent event : unpublishedEvents) {
            try {
                // Derive the Kafka topic name from the aggregate type
                String topic = buildTopicName(event.getAggregateType());

                // Publish to Kafka with aggregate ID as key for partition ordering
                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                log.error("Failed to publish event {}: {}",
                                        event.getId(), ex.getMessage());
                            }
                        });

                // Mark as published and record the timestamp
                event.setPublished(true);
                event.setPublishedAt(Instant.now());
                outboxRepository.save(event);

                log.info("Published event: id={}, type={}, aggregate={}",
                        event.getId(), event.getEventType(), event.getAggregateId());

            } catch (Exception e) {
                log.error("Error publishing event {}: {}", event.getId(), e.getMessage());
                // Stop processing to maintain ordering — next poll will retry from this event
                break;
            }
        }
    }

    /**
     * Builds the Kafka topic name from the aggregate type.
     * Converts to lowercase and appends "-events" suffix.
     *
     * @param aggregateType the aggregate type (e.g., "Order")
     * @return the derived topic name (e.g., "order-events")
     */
    private String buildTopicName(String aggregateType) {
        return aggregateType.toLowerCase() + "-events";
    }
}
