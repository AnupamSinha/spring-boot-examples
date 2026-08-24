package com.anupam.contract.consumer.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * REST client for communicating with the Payment Producer service.
 * Provides methods to retrieve payment information via HTTP calls.
 *
 * <p>This client uses Spring's {@link RestClient} for HTTP communication
 * and supports both auto-configured and custom base URL initialization.</p>
 *
 * @author Anupam
 */
@Component
public class PaymentClient {

    private final RestClient restClient;

    /**
     * Constructs a PaymentClient using Spring's auto-configured RestClient builder.
     * The base URL defaults to localhost:8080 for development purposes.
     *
     * @param restClientBuilder the Spring-provided RestClient builder
     */
    public PaymentClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    /**
     * Constructs a PaymentClient with a custom base URL.
     * Useful for testing scenarios where the producer runs on a different port.
     *
     * @param baseUrl the base URL of the payment producer service
     */
    public PaymentClient(String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Retrieves a payment by its unique identifier from the producer service.
     *
     * @param id the unique identifier of the payment to retrieve
     * @return the payment response containing payment details
     */
    public PaymentResponse getPayment(Long id) {
        return restClient.get()
                .uri("/api/payments/{id}", id)
                .retrieve()
                .body(PaymentResponse.class);
    }

    /**
     * Immutable record representing the payment response from the producer service.
     *
     * @param id       the unique payment identifier
     * @param orderId  the associated order identifier
     * @param amount   the payment amount
     * @param currency the currency code (e.g., USD, EUR)
     * @param status   the payment status (COMPLETED, PENDING, FAILED)
     */
    public record PaymentResponse(
            Long id,
            String orderId,
            java.math.BigDecimal amount,
            String currency,
            String status
    ) {}
}
