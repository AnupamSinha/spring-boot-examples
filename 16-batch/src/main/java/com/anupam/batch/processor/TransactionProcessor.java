package com.anupam.batch.processor;

import com.anupam.batch.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);
    private static final BigDecimal SUSPICIOUS_THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;

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
