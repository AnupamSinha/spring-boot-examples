package com.anupam.testcontainers.model;

import java.math.BigDecimal;

/**
 * Kafka event representing a payment status change.
 * Published to the "payments" topic and consumed by the PaymentEventConsumer.
 *
 * @param transactionId the transaction identifier
 * @param status        payment status (COMPLETED, FAILED, etc.)
 * @param amount        the payment amount
 * @author Anupam
 */
public record PaymentEvent(String transactionId, String status, BigDecimal amount) {}
