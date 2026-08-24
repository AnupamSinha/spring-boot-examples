package com.anupam.cqrs.projection;

import com.anupam.cqrs.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Projection that listens to domain events and rebuilds the read model.
 * <p>
 * This component is the bridge between the event store (write side) and the
 * query-optimized read model (read side). It consumes published events and
 * updates a relational table designed for fast querying.
 * </p>
 *
 * @author Anupam
 */
@Component
public class OrderProjection {

    private static final Logger log = LoggerFactory.getLogger(OrderProjection.class);

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs the projection with the required JDBC template.
     *
     * @param jdbcTemplate the JDBC template for executing SQL against the read model
     */
    public OrderProjection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Handles an {@link OrderCreatedEvent} by inserting a row into the read model table.
     * <p>
     * Uses {@code ON CONFLICT DO NOTHING} to ensure idempotency — replaying events
     * won't cause duplicate entries.
     * </p>
     *
     * @param event the order created domain event
     */
    @EventListener
    @Transactional
    public void on(OrderCreatedEvent event) {
        log.info("Projecting OrderCreatedEvent for order: {}", event.orderId());

        // Insert into the denormalized read model; ON CONFLICT ensures idempotent replays
        jdbcTemplate.update("""
            INSERT INTO orders_read_model (order_id, customer_id, item_count, total_amount, status, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (order_id) DO NOTHING
            """,
            event.orderId(),
            event.customerId(),
            event.items().size(),
            event.totalAmount(),
            "CREATED",
            java.sql.Timestamp.from(event.timestamp())
        );
    }
}
