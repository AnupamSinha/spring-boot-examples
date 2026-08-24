package com.anupam.ai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Detailed payment information returned by tool calls.
 */
public record PaymentInfo(
        String transactionId,
        String status,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp,
        String senderName,
        String receiverName
) {}
