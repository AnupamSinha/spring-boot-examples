package com.anupam.contract.producer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Payment(
        Long id,
        String orderId,
        BigDecimal amount,
        String currency,
        String status,
        LocalDateTime createdAt
) {
}
