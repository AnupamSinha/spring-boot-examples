package com.anupam.hexagonal.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity — pure Java, no JPA annotations, no Spring dependencies.
 * <p>
 * Contains business logic and enforces domain invariants. All state transitions
 * are validated to ensure the order is always in a consistent state.
 * </p>
 *
 * @author Anupam
 */
public class Order {

    private final String id;
    private final String customerId;
    private final List<LineItem> items;
    private OrderStatus status;
    private final Instant createdAt;

    /**
     * Constructs an order with all fields specified.
     *
     * @param id         the unique order identifier
     * @param customerId the customer who placed the order
     * @param items      the line items (defensively copied to immutable list)
     * @param status     the current order status
     * @param createdAt  when the order was created
     */
    public Order(String id, String customerId, List<LineItem> items, OrderStatus status, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.items = List.copyOf(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Factory method for creating new orders with PENDING status.
     *
     * @param customerId the customer placing the order
     * @param items      the line items (must not be empty)
     * @return a new Order instance with a generated ID and PENDING status
     * @throws IllegalArgumentException if items is null or empty
     */
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

    /**
     * Calculates the total order amount by summing all line item subtotals.
     *
     * @return the total monetary value of the order
     */
    public BigDecimal totalAmount() {
        return items.stream()
            .map(LineItem::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Confirms the order after successful payment processing.
     *
     * @throws IllegalStateException if the order is not in PAYMENT_PROCESSING status
     */
    public void confirm() {
        if (status != OrderStatus.PAYMENT_PROCESSING) {
            throw new IllegalStateException("Cannot confirm order in status: " + status);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    /**
     * Transitions the order to payment processing state.
     *
     * @throws IllegalStateException if the order is not in PENDING status
     */
    public void startPayment() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot start payment for order in status: " + status);
        }
        this.status = OrderStatus.PAYMENT_PROCESSING;
    }

    /**
     * Cancels the order. Cannot cancel orders that have already shipped or been delivered.
     *
     * @throws IllegalStateException if the order is in SHIPPED or DELIVERED status
     */
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

    /**
     * Value object representing a single line item within an order.
     *
     * @param productId   the product identifier
     * @param productName the human-readable product name
     * @param quantity    the quantity ordered
     * @param unitPrice   the price per unit
     */
    public record LineItem(String productId, String productName, int quantity, BigDecimal unitPrice) {

        /**
         * Computes the subtotal for this line item (quantity * unit price).
         *
         * @return the line item subtotal
         */
        public BigDecimal subtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
