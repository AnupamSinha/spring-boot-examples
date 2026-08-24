package com.anupam.contract.consumer;

import com.anupam.contract.consumer.client.PaymentClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Consumer-side contract test that verifies the PaymentClient correctly
 * interprets responses from the Payment Producer service.
 *
 * <p>Uses Spring Cloud Contract Stub Runner to start a stub server
 * from the producer's generated stubs, ensuring the consumer's
 * expectations match the producer's contract.</p>
 *
 * @author Anupam
 */
@SpringBootTest
@AutoConfigureStubRunner(
        ids = "com.anupam:contract-testing-producer:+:stubs:8090",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = "payment.service.url=http://localhost:8090")
class PaymentClientContractTest {

    /**
     * Verifies that the consumer can retrieve a payment from the producer stub
     * and that the response matches the expected contract format.
     * Validates field presence, format patterns, and valid value ranges.
     */
    @Test
    void shouldGetPaymentFromProducerStub() {
        // Given - create a client pointing to the stub server
        PaymentClient paymentClient = new PaymentClient("http://localhost:8090");

        // When - fetch payment with ID 1 from the stub
        PaymentClient.PaymentResponse payment = paymentClient.getPayment(1L);

        // Then - verify the response conforms to the contract
        assertThat(payment).isNotNull();
        assertThat(payment.id()).isEqualTo(1L);
        assertThat(payment.orderId()).matches("[A-Z]{3}-\\d{4}-\\d{3}");
        assertThat(payment.amount()).isGreaterThan(BigDecimal.ZERO);
        assertThat(payment.currency()).matches("[A-Z]{3}");
        assertThat(payment.status()).isIn("COMPLETED", "PENDING", "FAILED");
    }
}
