package com.anupam.testcontainers.controller;

import com.anupam.testcontainers.model.Payment;
import com.anupam.testcontainers.model.PaymentStatus;
import com.anupam.testcontainers.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for payment CRUD operations.
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

    /** GET /api/payments/{transactionId} - Retrieves a payment by transaction ID. */
    @GetMapping("/{transactionId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getPayment(transactionId));
    }

    /** GET /api/payments?status=... - Retrieves payments filtered by status. */
    @GetMapping
    public ResponseEntity<List<Payment>> getByStatus(@RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    /** POST /api/payments - Creates a new payment. */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(payment));
    }
}
