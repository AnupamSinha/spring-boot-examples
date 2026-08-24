package com.anupam.contract.producer.controller;

import com.anupam.contract.producer.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST controller exposing payment endpoints for the Contract Testing Producer.
 * Provides an in-memory payment store with sample data to demonstrate
 * contract-driven API development.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    /** Thread-safe in-memory store for payment records. */
    private final Map<Long, Payment> payments = new ConcurrentHashMap<>();

    /**
     * Initializes the controller with sample payment data.
     * Pre-populates two payment records for contract testing purposes.
     */
    public PaymentController() {
        // Seed sample payment data for contract verification
        payments.put(1L, new Payment(
                1L,
                "ORD-2024-001",
                new BigDecimal("99.99"),
                "USD",
                "COMPLETED",
                LocalDateTime.of(2024, 6, 15, 10, 30, 0)
        ));
        payments.put(2L, new Payment(
                2L,
                "ORD-2024-002",
                new BigDecimal("249.50"),
                "EUR",
                "PENDING",
                LocalDateTime.of(2024, 6, 16, 14, 45, 0)
        ));
    }

    /**
     * Retrieves a payment by its unique identifier.
     *
     * @param id the payment identifier
     * @return 200 OK with the payment if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = payments.get(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }
}
