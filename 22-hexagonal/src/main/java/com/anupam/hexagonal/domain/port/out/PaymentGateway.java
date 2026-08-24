package com.anupam.hexagonal.domain.port.out;

import java.math.BigDecimal;

/**
 * Output port — defines what the domain needs from a payment provider.
 * <p>
 * Could be implemented by Stripe, PayPal, or a mock in tests.
 * The domain depends only on this interface, not on any specific payment SDK.
 * </p>
 *
 * @author Anupam
 */
public interface PaymentGateway {

    /**
     * Charges a customer the specified amount in the given currency.
     *
     * @param customerId the customer to charge
     * @param amount     the monetary amount to charge
     * @param currency   the ISO currency code (e.g., "USD", "EUR")
     * @return the result of the payment attempt
     */
    PaymentResult charge(String customerId, BigDecimal amount, String currency);

    /**
     * Represents the outcome of a payment charge attempt.
     *
     * @param transactionId the unique transaction identifier from the payment provider
     * @param status        the payment outcome status
     */
    record PaymentResult(String transactionId, PaymentStatus status) {}

    /**
     * Possible outcomes of a payment charge.
     */
    enum PaymentStatus {
        /** Payment was processed successfully. */
        SUCCESS,
        /** Payment failed (insufficient funds, declined, etc.). */
        FAILED,
        /** Payment is still being processed asynchronously. */
        PENDING
    }
}
