package com.anupam.testcontainers;

import com.anupam.testcontainers.model.Payment;
import com.anupam.testcontainers.model.PaymentStatus;
import com.anupam.testcontainers.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for PaymentRepository using a real PostgreSQL container.
 * No H2, no mocks — tests run against the same database engine as production.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @Test
    void shouldSaveAndRetrievePayment() {
        Payment payment = new Payment("TXN-001", new BigDecimal("250.00"), PaymentStatus.COMPLETED);
        Payment saved = paymentRepository.save(payment);

        assertThat(saved.getId()).isNotNull();
        assertThat(paymentRepository.findByTransactionId("TXN-001"))
                .isPresent()
                .hasValueSatisfying(p -> {
                    assertThat(p.getAmount()).isEqualByComparingTo("250.00");
                    assertThat(p.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
                });
    }

    @Test
    void shouldFindPaymentsByStatus() {
        paymentRepository.save(new Payment("TXN-001", new BigDecimal("100.00"), PaymentStatus.COMPLETED));
        paymentRepository.save(new Payment("TXN-002", new BigDecimal("200.00"), PaymentStatus.PENDING));
        paymentRepository.save(new Payment("TXN-003", new BigDecimal("300.00"), PaymentStatus.COMPLETED));

        List<Payment> completed = paymentRepository.findByStatus(PaymentStatus.COMPLETED);
        assertThat(completed).hasSize(2);
        assertThat(completed).extracting(Payment::getTransactionId)
                .containsExactlyInAnyOrder("TXN-001", "TXN-003");
    }

    @Test
    void shouldFindHighValuePayments() {
        paymentRepository.save(new Payment("TXN-001", new BigDecimal("500.00"), PaymentStatus.COMPLETED));
        paymentRepository.save(new Payment("TXN-002", new BigDecimal("50.00"), PaymentStatus.COMPLETED));
        paymentRepository.save(new Payment("TXN-003", new BigDecimal("1500.00"), PaymentStatus.PENDING));

        List<Payment> highValue = paymentRepository.findHighValuePayments(new BigDecimal("100.00"));
        assertThat(highValue).hasSize(2);
        assertThat(highValue.get(0).getAmount()).isEqualByComparingTo("1500.00"); // ordered desc
    }

    @Test
    void shouldReturnEmptyForNonExistentTransaction() {
        assertThat(paymentRepository.findByTransactionId("DOES-NOT-EXIST")).isEmpty();
    }
}
