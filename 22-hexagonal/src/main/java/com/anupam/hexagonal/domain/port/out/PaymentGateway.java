package com.anupam.hexagonal.domain.port.out;

import java.math.BigDecimal;

/**
 * Output port — defines what the domain needs from a payment provider.
 * Could be Stripe, PayPal, or a mock in tests.
 */
public interface PaymentGateway {

    PaymentResult charge(String customerId, BigDecimal amount, String currency);

    record PaymentResult(String transactionId, PaymentStatus status) {}

    enum PaymentStatus {
        SUCCESS,
        FAILED,
        PENDING
    }
}
