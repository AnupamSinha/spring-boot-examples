package com.anupam.hexagonal.domain.model;

/**
 * Domain enum — no framework annotations.
 */
public enum OrderStatus {
    PENDING,
    PAYMENT_PROCESSING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
