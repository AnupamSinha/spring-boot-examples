package com.anupam.batch.processor;

import com.anupam.batch.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

/**
 * Processes transactions by validating amounts and flagging suspicious activity.
 * <p>
 * This processor applies two rules:
 * <ul>
 *   <li>Rejects transactions with negative amounts (returns {@code null} to filter them out).</li>
 *   <li>Flags transactions exceeding the suspicious threshold (10,000) as suspicious.</li>
 * </ul>
 * </p>
 *
 * @author Anupam
 */
public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);

    /** Threshold above which a transaction is considered suspicious. */
    private static final BigDecimal SUSPICIOUS_THRESHOLD = new BigDecimal("10000");

    /** Minimum valid transaction amount (zero). */
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;

    /**
     * Processes a single transaction by validating its amount and determining
     * whether it should be flagged as suspicious.
     *
     * @param transaction the transaction to process
     * @return the processed transaction with updated suspicious flag,
     *         or {@code null} if the transaction is rejected (negative amount)
     */
    @Override
    public Transaction process(Transaction transaction) {
        // Validate: reject negative amounts
        if (transaction.amount().compareTo(MIN_AMOUNT) < 0) {
            log.warn("Rejected transaction {} — negative amount: {}", transaction.id(), transaction.amount());
            return null; // filtered out
        }

        // Flag suspicious transactions (amount > 10,000)
        boolean suspicious = transaction.amount().compareTo(SUSPICIOUS_THRESHOLD) > 0;

        if (suspicious) {
            log.info("Flagged transaction {} as suspicious — amount: {}", transaction.id(), transaction.amount());
        }

        return transaction.withSuspicious(suspicious);
    }
}
