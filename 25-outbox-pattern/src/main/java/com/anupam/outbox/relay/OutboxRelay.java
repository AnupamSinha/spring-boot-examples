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

@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxRelay(OutboxRepository outboxRepository,
                       KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Polls the outbox table every 5 seconds for unpublished events.
     * Sends each event to the appropriate Kafka topic and marks it as published.
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void pollAndPublish() {
        List<OutboxEvent> unpublishedEvents =
                outboxRepository.findByPublishedFalseOrderByCreatedAtAsc();

        if (unpublishedEvents.isEmpty()) {
            return;
        }

        log.info("Found {} unpublished events to relay", unpublishedEvents.size());

        for (OutboxEvent event : unpublishedEvents) {
            try {
                String topic = buildTopicName(event.getAggregateType());

                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                log.error("Failed to publish event {}: {}",
                                        event.getId(), ex.getMessage());
                            }
                        });

                event.setPublished(true);
                event.setPublishedAt(Instant.now());
                outboxRepository.save(event);

                log.info("Published event: id={}, type={}, aggregate={}",
                        event.getId(), event.getEventType(), event.getAggregateId());

            } catch (Exception e) {
                log.error("Error publishing event {}: {}", event.getId(), e.getMessage());
                // Stop processing — next poll will retry from this event
                break;
            }
        }
    }

    private String buildTopicName(String aggregateType) {
        return aggregateType.toLowerCase() + "-events";
    }
}
