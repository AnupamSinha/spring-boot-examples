package com.anupam.observability.controller;

import com.anupam.observability.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST controller for payment processing with observability endpoints.
 *
 * Includes intentionally slow and error-producing endpoints for testing
 * alerting rules, latency histograms, and error rate dashboards.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** POST /api/payments - Processes a payment transfer and records metrics. */
    @PostMapping
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount) {

        Map<String, Object> result = paymentService.processPayment(from, to, amount);
        return ResponseEntity.ok(result);
    }

    /** GET /api/payments/{id} - Retrieves payment details by ID. */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    /**
     * GET /api/payments/slow - Simulates a slow endpoint (2s delay).
     * Useful for testing latency-based alerts and SLO dashboards.
     */
    @GetMapping("/slow")
    public ResponseEntity<Map<String, String>> slowEndpoint() throws InterruptedException {
        Thread.sleep(2000);
        return ResponseEntity.ok(Map.of("message", "This was slow on purpose"));
    }

    /**
     * GET /api/payments/error - Always throws an exception.
     * Useful for testing error rate alerts and error tracking dashboards.
     */
    @GetMapping("/error")
    public ResponseEntity<Void> errorEndpoint() {
        throw new RuntimeException("Simulated error for observability demo");
    }
}
