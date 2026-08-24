package com.anupam.events;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentCompletedEvent(
        String paymentId,
        String orderId,
        String status,
        BigDecimal amount,
        Instant processedAt
) {}
