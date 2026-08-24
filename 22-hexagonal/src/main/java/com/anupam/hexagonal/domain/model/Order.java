package com.anupam.hexagonal.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity — pure Java, no JPA annotations, no Spring dependencies.
 * Contains business logic and invariants.
 */
public class Order {

    private final String id;
    private final String customerId;
    private final List<LineItem> items;
    private OrderStatus status;
    private final Instant createdAt;

    public Order(String id, String customerId, List<LineItem> items, OrderStatus status, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.items = List.copyOf(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    /** Factory method for creating new orders */
    public static Order create(String customerId, List<LineItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        return new Order(
            UUID.randomUUID().toString(),
            customerId,
            items,
            OrderStatus.PENDING,
            Instant.now()
        );
    }

    /** Domain logic: calculate total */
    public BigDecimal totalAmount() {
        return items.stream()
            .map(LineItem::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Domain logic: confirm order after payment */
    public void confirm() {
        if (status != OrderStatus.PAYMENT_PROCESSING) {
            throw new IllegalStateException("Cannot confirm order in status: " + status);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    /** Domain logic: mark payment processing */
    public void startPayment() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot start payment for order in status: " + status);
        }
        this.status = OrderStatus.PAYMENT_PROCESSING;
    }

    /** Domain logic: cancel order */
    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel order in status: " + status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<LineItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    /** Value object for line items */
    public record LineItem(String productId, String productName, int quantity, BigDecimal unitPrice) {
        public BigDecimal subtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
