package com.anupam.cqrs.event.store;

import java.util.List;

/**
 * Event Store interface — the source of truth in an event-sourced system.
 * <p>
 * All state changes are persisted as an ordered sequence of events. The event store
 * provides append-only semantics, and events can be replayed to reconstruct aggregate state.
 * </p>
 *
 * @author Anupam
 */
public interface EventStore {

    /**
     * Appends an event to the stream of the given aggregate.
     *
     * @param aggregateId the unique identifier of the aggregate (e.g., order ID)
     * @param event       the domain event to persist
     */
    void append(String aggregateId, Object event);

    /**
     * Retrieves all events for a specific aggregate, ordered chronologically.
     *
     * @param aggregateId the unique identifier of the aggregate
     * @return the list of events belonging to the aggregate
     */
    List<Object> getEvents(String aggregateId);

    /**
     * Retrieves all events across all aggregates.
     *
     * @return the complete list of stored events
     */
    List<Object> getAllEvents();
}
