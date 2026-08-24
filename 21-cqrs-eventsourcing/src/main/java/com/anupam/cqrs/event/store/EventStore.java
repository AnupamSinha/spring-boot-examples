package com.anupam.cqrs.event.store;

import java.util.List;

/**
 * Event Store interface — the source of truth in an event-sourced system.
 * All state changes are persisted as an ordered sequence of events.
 */
public interface EventStore {

    void append(String aggregateId, Object event);

    List<Object> getEvents(String aggregateId);

    List<Object> getAllEvents();
}
