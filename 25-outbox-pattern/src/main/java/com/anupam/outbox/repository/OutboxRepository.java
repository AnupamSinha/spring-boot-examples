package com.anupam.outbox.repository;

import com.anupam.outbox.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link OutboxEvent} entities.
 * <p>
 * Provides the core query used by the outbox relay to find events
 * that have not yet been published to the message broker.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    /**
     * Finds all unpublished outbox events ordered by creation time (oldest first).
     * This ensures FIFO processing by the outbox relay.
     *
     * @return list of unpublished events in chronological order
     */
    List<OutboxEvent> findByPublishedFalseOrderByCreatedAtAsc();
}
