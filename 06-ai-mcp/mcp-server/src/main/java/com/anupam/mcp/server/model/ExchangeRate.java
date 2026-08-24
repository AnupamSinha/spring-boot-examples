package com.anupam.mcp.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Exchange rate between two currencies with a timestamp.
 *
 * @param from      source currency code (e.g., "USD")
 * @param to        target currency code (e.g., "EUR")
 * @param rate      the conversion rate
 * @param timestamp when the rate was computed
 * @author Anupam
 */
public record ExchangeRate(
        String from,
        String to,
        BigDecimal rate,
        LocalDateTime timestamp
) {}
