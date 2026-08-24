package com.anupam.hexagonal.domain.model;

/**
 * Domain enum representing the lifecycle states of an order.
 * <p>
 * No framework annotations — this is a pure domain concept. The valid
 * state transitions are enforced by the {@link Order} entity.
 * </p>
 *
 * @author Anupam
 */
public enum OrderStatus {
    /** Order has been created but payment has not yet started. */
    PENDING,
    /** Payment is being processed by the payment gateway. */
    PAYMENT_PROCESSING,
    /** Payment succeeded and the order is confirmed. */
    CONFIRMED,
    /** Order has been shipped to the customer. */
    SHIPPED,
    /** Order has been delivered to the customer. */
    DELIVERED,
    /** Order has been cancelled. */
    CANCELLED
}
