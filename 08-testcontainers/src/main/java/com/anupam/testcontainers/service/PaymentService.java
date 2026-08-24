package com.anupam.testcontainers.service;

import com.anupam.testcontainers.model.Payment;
import com.anupam.testcontainers.model.PaymentStatus;
import com.anupam.testcontainers.repository.PaymentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for payment operations.
 *
 * Includes caching for individual payment lookups to reduce
 * database load on frequently accessed transactions.
 *
 * @author Anupam
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Retrieves a payment by transaction ID with caching.
     *
     * @param transactionId the unique transaction identifier
     * @return the payment entity
     * @throws RuntimeException if the payment is not found
     */
    @Cacheable(value = "payments", key = "#transactionId")
    public Payment getPayment(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + transactionId));
    }

    /** Persists a new payment entity. */
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    /** Retrieves all payments with the given status. */
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
}
