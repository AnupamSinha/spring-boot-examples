package com.anupam.hexagonal.adapter.out.payment;

import com.anupam.hexagonal.domain.port.out.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Output adapter — implements the PaymentGateway port using Stripe.
 * In a real app, this would call the Stripe SDK.
 */
@Component
public class StripePaymentGateway implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentGateway.class);

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
