package com.anupam.hexagonal.adapter.out.payment;

import com.anupam.hexagonal.domain.port.out.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Output adapter — implements the {@link PaymentGateway} port using Stripe.
 * <p>
 * In a production application, this adapter would use the Stripe SDK to process
 * real payments. The current implementation simulates a successful charge for
 * demonstration purposes.
 * </p>
 *
 * @author Anupam
 */
@Component
public class StripePaymentGateway implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentGateway.class);

    /**
     * Charges the specified customer for the given amount.
     * <p>
     * This is a simulated implementation. In production, it would invoke
     * Stripe's Charge API via the SDK.
     * </p>
     *
     * @param customerId the customer to charge
     * @param amount     the monetary amount to charge
     * @param currency   the currency code (e.g., "USD")
     * @return the payment result containing a transaction ID and status
     */
    @Override
    public PaymentResult charge(String customerId, BigDecimal amount, String currency) {
        log.info("Charging customer {} amount {} {}", customerId, amount, currency);

        // Simulated Stripe API call
        // In production, use: Stripe.apiKey = stripeApiKey;
        //                      Charge.create(params);

        String transactionId = "txn_" + UUID.randomUUID().toString().substring(0, 8);
        log.info("Payment successful: {}", transactionId);

        return new PaymentResult(transactionId, PaymentStatus.SUCCESS);
    }
}
