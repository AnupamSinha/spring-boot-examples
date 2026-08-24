package com.anupam.observability.controller;

import com.anupam.observability.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount) {

        Map<String, Object> result = paymentService.processPayment(from, to, amount);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/slow")
    public ResponseEntity<Map<String, String>> slowEndpoint() throws InterruptedException {
        // Simulates a slow endpoint for observability testing
        Thread.sleep(2000);
        return ResponseEntity.ok(Map.of("message", "This was slow on purpose"));
    }

    @GetMapping("/error")
    public ResponseEntity<Void> errorEndpoint() {
        // Simulates an error for observability testing
        throw new RuntimeException("Simulated error for observability demo");
    }
}
