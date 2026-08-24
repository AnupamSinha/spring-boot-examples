package com.anupam.testcontainers.repository;

import com.anupam.testcontainers.model.Payment;
import com.anupam.testcontainers.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for payment entity persistence.
 *
 * Provides custom query methods tested against a real PostgreSQL
 * container via Testcontainers.
 *
 * @author Anupam
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /** Finds a payment by its unique transaction ID. */
    Optional<Payment> findByTransactionId(String transactionId);

    /** Finds all payments with a given status. */
    List<Payment> findByStatus(PaymentStatus status);

    /** Finds payments exceeding a minimum amount, ordered by amount descending. */
    @Query("SELECT p FROM Payment p WHERE p.amount > :minAmount ORDER BY p.amount DESC")
    List<Payment> findHighValuePayments(BigDecimal minAmount);

    /** Finds payments matching both a status and minimum amount threshold. */
    List<Payment> findByStatusAndAmountGreaterThan(PaymentStatus status, BigDecimal amount);
}
