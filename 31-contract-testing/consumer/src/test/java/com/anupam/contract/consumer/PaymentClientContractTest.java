package com.anupam.contract.consumer;

import com.anupam.contract.consumer.client.PaymentClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(
        ids = "com.anupam:contract-testing-producer:+:stubs:8090",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = "payment.service.url=http://localhost:8090")
class PaymentClientContractTest {

    @Test
    void shouldGetPaymentFromProducerStub() {
        // Given
        PaymentClient paymentClient = new PaymentClient("http://localhost:8090");

        // When
        PaymentClient.PaymentResponse payment = paymentClient.getPayment(1L);

        // Then
        assertThat(payment).isNotNull();
        assertThat(payment.id()).isEqualTo(1L);
        assertThat(payment.orderId()).matches("[A-Z]{3}-\\d{4}-\\d{3}");
        assertThat(payment.amount()).isGreaterThan(BigDecimal.ZERO);
        assertThat(payment.currency()).matches("[A-Z]{3}");
        assertThat(payment.status()).isIn("COMPLETED", "PENDING", "FAILED");
    }
}
