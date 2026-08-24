package com.anupam.observability.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Payment service instrumented with full observability.
 *
 * Demonstrates:
 * - Counter: tracks total number of payments processed
 * - Timer: measures payment processing duration distribution
 * - Observation: creates spans for distributed tracing with key-value context
 * - Structured logging: logs with contextual payment data for search/filtering
 *
 * @author Anupam
 */
@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final Counter paymentCounter;
    private final Timer paymentTimer;
    private final ObservationRegistry observationRegistry;

    public PaymentService(MeterRegistry meterRegistry, ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;

        // Custom counter to track total processed payments
        this.paymentCounter = Counter.builder("payments.processed.total")
                .description("Total payments processed")
                .register(meterRegistry);

        // Custom timer to measure processing latency distribution
        this.paymentTimer = Timer.builder("payments.processing.duration")
                .description("Payment processing duration")
                .register(meterRegistry);
    }

    /**
     * Processes a payment within an Observation span for tracing.
     * Payments over $10,000 are flagged for manual REVIEW.
     */
    public Map<String, Object> processPayment(String from, String to, BigDecimal amount) {
        return Observation.createNotStarted("payment.process", observationRegistry)
                .lowCardinalityKeyValue("payment.method", "transfer")
                .observe(() -> {
                    log.info("Processing payment: {} → {}, amount: {}", from, to, amount);

                    return paymentTimer.record(() -> {
                        // Simulate variable processing time (100-600ms)
                        simulateProcessing();

                        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
                        String status = amount.compareTo(new BigDecimal("10000")) > 0 ? "REVIEW" : "COMPLETED";

                        paymentCounter.increment();
                        log.info("Payment completed: {} status: {}", paymentId, status);

                        return Map.<String, Object>of(
                                "paymentId", paymentId,
                                "from", from,
                                "to", to,
                                "amount", amount.toString(),
                                "status", status,
                                "timestamp", LocalDateTime.now().toString()
                        );
                    });
                });
    }

    /** Retrieves payment details by ID (simulated lookup). */
    public Map<String, Object> getPayment(String id) {
        log.info("Looking up payment: {}", id);
        return Map.of(
                "paymentId", id,
                "status", "COMPLETED",
                "amount", "250.00",
                "timestamp", LocalDateTime.now().toString()
        );
    }

    /** Simulates variable-latency database/network call. */
    private void simulateProcessing() {
        try {
            Thread.sleep((long) (Math.random() * 500 + 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
