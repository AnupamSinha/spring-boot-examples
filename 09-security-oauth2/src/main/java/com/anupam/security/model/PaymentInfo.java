package com.anupam.security.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentInfo(
        String transactionId,
        BigDecimal amount,
        String status,
        String owner,
        LocalDateTime timestamp
) {}
