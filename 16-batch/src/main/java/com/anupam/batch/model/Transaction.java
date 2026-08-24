package com.anupam.batch.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
        Long id,
        String accountId,
        BigDecimal amount,
        String type,
        LocalDateTime timestamp,
        boolean suspicious
) {
    public Transaction withSuspicious(boolean suspicious) {
        return new Transaction(id, accountId, amount, type, timestamp, suspicious);
    }

    public Transaction withId(Long id) {
        return new Transaction(id, accountId, amount, type, timestamp, suspicious);
    }
}
