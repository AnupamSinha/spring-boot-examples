package com.anupam.streams.model;

import java.time.Instant;

/**
 * Immutable record representing an enriched payment after stream processing.
 * Extends the raw payment data with a processing timestamp and a derived status
 * based on the payment amount threshold.
 *
 * @param id          the unique payment identifier (UUID)
 * @param currency    the ISO 4217 currency code (e.g., USD, EUR, GBP)
 * @param amount      the payment amount
 * @param status      the derived status: "HIGH_VALUE" if amount exceeds 10000, otherwise "NORMAL"
 * @param processedAt the timestamp when the payment was processed by the stream topology
 *
 * @author Anupam
 */
public record EnrichedPayment(String id, String currency, double amount, String status, Instant processedAt) {
}
