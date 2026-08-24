package com.anupam.testcontainers.repository;

import com.anupam.testcontainers.model.Payment;
import com.anupam.testcontainers.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.amount > :minAmount ORDER BY p.amount DESC")
    List<Payment> findHighValuePayments(BigDecimal minAmount);

    List<Payment> findByStatusAndAmountGreaterThan(PaymentStatus status, BigDecimal amount);
}
