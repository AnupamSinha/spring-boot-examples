package com.anupam.cqrs.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Domain event representing that an order has been created.
 * <p>
 * In an event-sourced system, this event is the immutable record of the state change.
 * It captures all information needed to reconstruct the order's creation.
 * </p>
 *
 * @param eventId     unique identifier of this event instance
 * @param orderId     aggregate identifier (the order this event belongs to)
 * @param customerId  the customer who placed the order
 * @param items       the line items included in the order
 * @param totalAmount the computed total value of the order
 * @param timestamp   when the event occurred
 *
 * @author Anupam
 */
public record OrderCreatedEvent(
    String eventId,
    String orderId,
    String customerId,
    List<EventItem> items,
    BigDecimal totalAmount,
    Instant timestamp
) {
    /**
     * Represents a single line item within the order creation event.
     *
     * @param productId the product identifier
     * @param quantity  the quantity ordered
     * @param price     the unit price at the time of order
     */
    public record EventItem(
        String productId,
        int quantity,
        BigDecimal price
    ) {}
}
