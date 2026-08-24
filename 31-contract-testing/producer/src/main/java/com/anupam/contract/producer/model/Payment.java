package com.anupam.contract.producer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable record representing a payment entity in the contract testing producer.
 * Defines the contract structure that consumers rely on.
 *
 * @param id        the unique payment identifier
 * @param orderId   the associated order identifier (format: XXX-YYYY-ZZZ)
 * @param amount    the payment amount
 * @param currency  the ISO 4217 currency code (e.g., USD, EUR)
 * @param status    the payment status (COMPLETED, PENDING, FAILED)
 * @param createdAt the timestamp when the payment was created
 *
 * @author Anupam
 */
public record Payment(
        Long id,
        String orderId,
        BigDecimal amount,
        String currency,
        String status,
        LocalDateTime createdAt
) {
}
