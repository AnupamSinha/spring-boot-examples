package com.anupam.events;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Event published when payment processing completes (success or failure).
 * Consumed by notification-service for sending payment receipts.
 *
 * @param paymentId   unique payment identifier
 * @param orderId     the order this payment belongs to
 * @param status      result: COMPLETED or FAILED
 * @param amount      the payment amount
 * @param processedAt timestamp when processing completed
 * @author Anupam
 */
public record PaymentCompletedEvent(
        String paymentId,
        String orderId,
        String status,
        BigDecimal amount,
        Instant processedAt
) {}
