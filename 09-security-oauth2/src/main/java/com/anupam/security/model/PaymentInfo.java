package com.anupam.security.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment information returned to authenticated users.
 *
 * @param transactionId unique transaction identifier
 * @param amount        payment amount
 * @param status        payment status (COMPLETED, PENDING, FAILED)
 * @param owner         the username who owns this payment
 * @param timestamp     when the payment was processed
 * @author Anupam
 */
public record PaymentInfo(
        String transactionId,
        BigDecimal amount,
        String status,
        String owner,
        LocalDateTime timestamp
) {}
