package com.anupam.cqrs.event.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * MongoDB-backed implementation of the {@link EventStore} interface.
 * <p>
 * Events are wrapped in an {@link EventEnvelope} document that includes metadata
 * such as aggregate ID, event type, and storage timestamp. All envelopes are
 * stored in the "events" collection.
 * </p>
 *
 * @author Anupam
 */
@Repository
public class MongoEventStore implements EventStore {

    private final MongoTemplate mongoTemplate;

    /**
     * Constructs the MongoDB event store with the required {@link MongoTemplate}.
     *
     * @param mongoTemplate the Spring Data MongoDB template for collection operations
     */
    public MongoEventStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Wraps the event in an envelope and persists it to the "events" collection.
     *
     * @param aggregateId the aggregate identifier the event belongs to
     * @param event       the domain event to store
     */
    @Override
    public void append(String aggregateId, Object event) {
        // Wrap the raw event in an envelope with metadata for querying
        var envelope = new EventEnvelope(
            UUID.randomUUID().toString(),
            aggregateId,
            event.getClass().getSimpleName(),
            event,
            Instant.now()
        );
        mongoTemplate.save(envelope, "events");
    }

    /**
     * Queries all events for a given aggregate from the "events" collection.
     *
     * @param aggregateId the aggregate identifier to filter by
     * @return the list of event payloads for the aggregate
     */
    @Override
    public List<Object> getEvents(String aggregateId) {
        var query = Query.query(Criteria.where("aggregateId").is(aggregateId));
        return mongoTemplate.find(query, EventEnvelope.class, "events")
            .stream()
            .map(EventEnvelope::payload)
            .toList();
    }

    /**
     * Retrieves all events from the "events" collection regardless of aggregate.
     *
     * @return the complete list of stored event payloads
     */
    @Override
    public List<Object> getAllEvents() {
        return mongoTemplate.findAll(EventEnvelope.class, "events")
            .stream()
            .map(EventEnvelope::payload)
            .toList();
    }

    /**
     * Internal envelope document wrapping a domain event with storage metadata.
     *
     * @param id          unique envelope identifier
     * @param aggregateId the aggregate this event belongs to
     * @param eventType   the simple class name of the event for type discrimination
     * @param payload     the serialized domain event
     * @param storedAt    timestamp when the event was persisted
     */
    @Document(collection = "events")
    record EventEnvelope(
        @Id String id,
        String aggregateId,
        String eventType,
        Object payload,
        Instant storedAt
    ) {}
}
