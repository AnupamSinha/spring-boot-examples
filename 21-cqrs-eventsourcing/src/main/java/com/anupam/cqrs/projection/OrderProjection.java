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
 * This is the bridge between the event store and the query-optimized read model.
 */
@Component
public class OrderProjection {

    private static final Logger log = LoggerFactory.getLogger(OrderProjection.class);

    private final JdbcTemplate jdbcTemplate;

    public OrderProjection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    @Transactional
    public void on(OrderCreatedEvent event) {
        log.info("Projecting OrderCreatedEvent for order: {}", event.orderId());

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
