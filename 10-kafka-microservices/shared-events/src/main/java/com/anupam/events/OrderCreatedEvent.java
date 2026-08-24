package com.anupam.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Event published when a new order is created.
 * Consumed by payment-service and notification-service.
 *
 * @param orderId     unique order identifier
 * @param customerId  the customer who placed the order
 * @param totalAmount computed total from all line items
 * @param items       list of ordered items
 * @param createdAt   timestamp when the order was created
 * @author Anupam
 */
public record OrderCreatedEvent(
        String orderId,
        String customerId,
        BigDecimal totalAmount,
        List<OrderItem> items,
        Instant createdAt
) {}
