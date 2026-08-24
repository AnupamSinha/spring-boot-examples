package com.anupam.mcp.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment information record returned by the payment status tool.
 *
 * @param transactionId unique transaction identifier
 * @param status        payment status (COMPLETED, PENDING, FAILED)
 * @param amount        payment amount
 * @param currency      currency code
 * @param timestamp     when the payment was processed
 * @param senderName    name of the sender
 * @param receiverName  name of the receiver
 * @author Anupam
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
