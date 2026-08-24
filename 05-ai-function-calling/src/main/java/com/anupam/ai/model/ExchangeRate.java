package com.anupam.ai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Exchange rate between two currencies.
 */
public record ExchangeRate(
        String from,
        String to,
        BigDecimal rate,
        LocalDateTime timestamp
) {}
