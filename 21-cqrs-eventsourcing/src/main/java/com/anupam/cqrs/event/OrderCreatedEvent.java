package com.anupam.cqrs.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
    String eventId,
    String orderId,
    String customerId,
    List<EventItem> items,
    BigDecimal totalAmount,
    Instant timestamp
) {
    public record EventItem(
        String productId,
        int quantity,
        BigDecimal price
    ) {}
}
