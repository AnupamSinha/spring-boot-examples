package com.anupam.streams.model;

import java.time.Instant;

public record EnrichedPayment(String id, String currency, double amount, String status, Instant processedAt) {
}
