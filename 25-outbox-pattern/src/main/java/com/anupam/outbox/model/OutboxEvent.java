package com.anupam.outbox.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity representing an event in the transactional outbox table.
 * <p>
 * Each row is an event that needs to be published to a message broker. The outbox relay
 * polls for unpublished events and sends them to Kafka, then marks them as published.
 * This ensures at-least-once delivery without dual writes.
 * </p>
 *
 * @author Anupam
 */
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    /** Unique event identifier (UUID for global uniqueness). */
    @Id
    private UUID id;

    /** The type of aggregate this event relates to (e.g., "Order"). */
    @Column(nullable = false)
    private String aggregateType;

    /** The identifier of the specific aggregate instance. */
    @Column(nullable = false)
    private String aggregateId;

    /** The type of event (e.g., "OrderCreated", "OrderConfirmed"). */
    @Column(nullable = false)
    private String eventType;

    /** The JSON-serialized event payload. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    /** When the event was created (written to the outbox). */
    @Column(nullable = false)
    private Instant createdAt;

    /** Whether the event has been successfully published to the message broker. */
    @Column(nullable = false)
    private boolean published;

    /** When the event was published (null if not yet published). */
    private Instant publishedAt;

    /** Default constructor — initializes with UUID, current timestamp, and unpublished state. */
    public OutboxEvent() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.published = false;
    }

    /**
     * Constructs an outbox event with the given metadata and payload.
     *
     * @param aggregateType the aggregate type (e.g., "Order")
     * @param aggregateId   the aggregate instance ID
     * @param eventType     the event type name
     * @param payload       the JSON-serialized event data
     */
    public OutboxEvent(String aggregateType, String aggregateId,
                       String eventType, String payload) {
        this();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
}
