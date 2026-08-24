package com.anupam.contract.consumer.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    public PaymentClient(String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public PaymentResponse getPayment(Long id) {
        return restClient.get()
                .uri("/api/payments/{id}", id)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public record PaymentResponse(
            Long id,
            String orderId,
            java.math.BigDecimal amount,
            String currency,
            String status
    ) {}
}
