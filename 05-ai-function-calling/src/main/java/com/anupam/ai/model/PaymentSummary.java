package com.anupam.ai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lightweight payment summary for list operations.
 */
public record PaymentSummary(
        String transactionId,
        String status,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp
) {}
