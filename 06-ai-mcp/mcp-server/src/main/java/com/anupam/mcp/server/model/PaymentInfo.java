package com.anupam.mcp.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentInfo(
        String transactionId,
        String status,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp,
        String senderName,
        String receiverName
) {}
