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

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Map<Long, Payment> payments = new ConcurrentHashMap<>();

    public PaymentController() {
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

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = payments.get(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }
}
