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

@Repository
public class MongoEventStore implements EventStore {

    private final MongoTemplate mongoTemplate;

    public MongoEventStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void append(String aggregateId, Object event) {
        var envelope = new EventEnvelope(
            UUID.randomUUID().toString(),
            aggregateId,
            event.getClass().getSimpleName(),
            event,
            Instant.now()
        );
        mongoTemplate.save(envelope, "events");
    }

    @Override
    public List<Object> getEvents(String aggregateId) {
        var query = Query.query(Criteria.where("aggregateId").is(aggregateId));
        return mongoTemplate.find(query, EventEnvelope.class, "events")
            .stream()
            .map(EventEnvelope::payload)
            .toList();
    }

    @Override
    public List<Object> getAllEvents() {
        return mongoTemplate.findAll(EventEnvelope.class, "events")
            .stream()
            .map(EventEnvelope::payload)
            .toList();
    }

    @Document(collection = "events")
    record EventEnvelope(
        @Id String id,
        String aggregateId,
        String eventType,
        Object payload,
        Instant storedAt
    ) {}
}
