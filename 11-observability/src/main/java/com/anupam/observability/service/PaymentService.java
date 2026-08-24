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

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final Counter paymentCounter;
    private final Timer paymentTimer;
    private final ObservationRegistry observationRegistry;

    public PaymentService(MeterRegistry meterRegistry, ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;

        this.paymentCounter = Counter.builder("payments.processed.total")
                .description("Total payments processed")
                .register(meterRegistry);

        this.paymentTimer = Timer.builder("payments.processing.duration")
                .description("Payment processing duration")
                .register(meterRegistry);
    }

    public Map<String, Object> processPayment(String from, String to, BigDecimal amount) {
        return Observation.createNotStarted("payment.process", observationRegistry)
                .lowCardinalityKeyValue("payment.method", "transfer")
                .observe(() -> {
                    log.info("Processing payment: {} → {}, amount: {}", from, to, amount);

                    return paymentTimer.record(() -> {
                        // Simulate processing
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

    public Map<String, Object> getPayment(String id) {
        log.info("Looking up payment: {}", id);
        return Map.of(
                "paymentId", id,
                "status", "COMPLETED",
                "amount", "250.00",
                "timestamp", LocalDateTime.now().toString()
        );
    }

    private void simulateProcessing() {
        try {
            Thread.sleep((long) (Math.random() * 500 + 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
