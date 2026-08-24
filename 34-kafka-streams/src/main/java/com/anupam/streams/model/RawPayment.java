package com.anupam.streams.model;

/**
 * Immutable record representing a raw payment event as received from the input Kafka topic.
 * Contains the essential payment fields before any enrichment or processing.
 *
 * @param id       the unique payment identifier (UUID)
 * @param currency the ISO 4217 currency code (e.g., USD, EUR, GBP)
 * @param amount   the payment amount
 *
 * @author Anupam
 */
public record RawPayment(String id, String currency, double amount) {
}
