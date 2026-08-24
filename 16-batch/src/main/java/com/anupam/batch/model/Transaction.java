package com.anupam.batch.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable record representing a financial transaction.
 * <p>
 * Uses Java record syntax to provide a concise, immutable data carrier
 * for transaction information processed by the batch job.
 * </p>
 *
 * @param id         unique identifier for the transaction
 * @param accountId  the account associated with this transaction
 * @param amount     monetary amount of the transaction
 * @param type       transaction type (e.g., credit, debit)
 * @param timestamp  date and time when the transaction occurred
 * @param suspicious flag indicating whether the transaction is flagged as suspicious
 *
 * @author Anupam
 */
public record Transaction(
        Long id,
        String accountId,
        BigDecimal amount,
        String type,
        LocalDateTime timestamp,
        boolean suspicious
) {
    /**
     * Creates a copy of this transaction with the specified suspicious flag.
     *
     * @param suspicious the new suspicious status to apply
     * @return a new {@link Transaction} instance with the updated suspicious flag
     */
    public Transaction withSuspicious(boolean suspicious) {
        return new Transaction(id, accountId, amount, type, timestamp, suspicious);
    }

    /**
     * Creates a copy of this transaction with the specified ID.
     *
     * @param id the new identifier to assign
     * @return a new {@link Transaction} instance with the updated ID
     */
    public Transaction withId(Long id) {
        return new Transaction(id, accountId, amount, type, timestamp, suspicious);
    }
}
