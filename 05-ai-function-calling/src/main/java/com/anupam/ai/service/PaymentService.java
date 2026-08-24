package com.anupam.ai.service;

import com.anupam.ai.model.PaymentInfo;
import com.anupam.ai.model.PaymentSummary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simulated payment service with in-memory data.
 * In production, this would query a database or call a downstream service.
 */
@Service
public class PaymentService {

    private final Map<String, PaymentInfo> payments = new ConcurrentHashMap<>();
    private final Map<Long, List<PaymentSummary>> customerPayments = new ConcurrentHashMap<>();

    public PaymentService() {
        initializeSampleData();
    }

    public PaymentInfo findByTransactionId(String transactionId) {
        return payments.get(transactionId);
    }

    public List<PaymentSummary> getRecentPayments(Long customerId, int limit) {
        List<PaymentSummary> all = customerPayments.getOrDefault(customerId, List.of());
        return all.stream()
                .sorted((a, b) -> b.timestamp().compareTo(a.timestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private void initializeSampleData() {
        payments.put("TXN-9042", new PaymentInfo(
                "TXN-9042", "COMPLETED", new BigDecimal("250.00"), "USD",
                LocalDateTime.of(2026, 8, 20, 14, 30, 0),
                "Alice Johnson", "Bob Smith"
        ));
        payments.put("TXN-9043", new PaymentInfo(
                "TXN-9043", "PENDING", new BigDecimal("1200.50"), "USD",
                LocalDateTime.of(2026, 8, 21, 9, 15, 0),
                "Charlie Brown", "Diana Prince"
        ));
        payments.put("TXN-9044", new PaymentInfo(
                "TXN-9044", "FAILED", new BigDecimal("75.00"), "EUR",
                LocalDateTime.of(2026, 8, 21, 16, 45, 0),
                "Eve Wilson", "Frank Castle"
        ));
        payments.put("TXN-9045", new PaymentInfo(
                "TXN-9045", "COMPLETED", new BigDecimal("500.00"), "GBP",
                LocalDateTime.of(2026, 8, 19, 11, 0, 0),
                "Grace Hopper", "Alan Turing"
        ));
        payments.put("TXN-9046", new PaymentInfo(
                "TXN-9046", "COMPLETED", new BigDecimal("89.99"), "USD",
                LocalDateTime.of(2026, 8, 18, 8, 30, 0),
                "Alice Johnson", "Eve Wilson"
        ));

        // Customer 42 payments
        customerPayments.put(42L, List.of(
                new PaymentSummary("TXN-9042", "COMPLETED", new BigDecimal("250.00"), "USD",
                        LocalDateTime.of(2026, 8, 20, 14, 30, 0)),
                new PaymentSummary("TXN-9043", "PENDING", new BigDecimal("1200.50"), "USD",
                        LocalDateTime.of(2026, 8, 21, 9, 15, 0)),
                new PaymentSummary("TXN-9046", "COMPLETED", new BigDecimal("89.99"), "USD",
                        LocalDateTime.of(2026, 8, 18, 8, 30, 0))
        ));

        // Customer 101 payments
        customerPayments.put(101L, List.of(
                new PaymentSummary("TXN-9044", "FAILED", new BigDecimal("75.00"), "EUR",
                        LocalDateTime.of(2026, 8, 21, 16, 45, 0)),
                new PaymentSummary("TXN-9045", "COMPLETED", new BigDecimal("500.00"), "GBP",
                        LocalDateTime.of(2026, 8, 19, 11, 0, 0))
        ));
    }
}
