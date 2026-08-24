package com.anupam.ai.service;

import com.anupam.ai.model.ExchangeRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Simulated exchange rate service providing currency conversion rates.
 *
 * Stores rates relative to USD and computes cross-rates on-demand.
 * In production, this would call an external API like exchangeratesapi.io or fixer.io.
 *
 * @author Anupam
 */
@Service
public class ExchangeRateService {

    /** Static rate table: currency code -> rate relative to USD. */
    private static final Map<String, BigDecimal> RATES_TO_USD = Map.of(
            "USD", BigDecimal.ONE,
            "EUR", new BigDecimal("0.92"),
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("149.50"),
            "INR", new BigDecimal("83.12"),
            "CAD", new BigDecimal("1.36"),
            "AUD", new BigDecimal("1.53")
    );

    /**
     * Computes the exchange rate between two currencies via USD cross-rate.
     *
     * @param from source currency code (e.g., "USD")
     * @param to   target currency code (e.g., "EUR")
     * @return the computed exchange rate with a current timestamp
     * @throws IllegalArgumentException if either currency is unsupported
     */
    public ExchangeRate getRate(String from, String to) {
        BigDecimal fromToUsd = RATES_TO_USD.get(from.toUpperCase());
        BigDecimal toToUsd = RATES_TO_USD.get(to.toUpperCase());

        if (fromToUsd == null || toToUsd == null) {
            throw new IllegalArgumentException(
                    "Unsupported currency. Supported: " + RATES_TO_USD.keySet());
        }

        // Convert: from -> USD -> to (cross-rate calculation)
        BigDecimal rate = toToUsd.divide(fromToUsd, 6, RoundingMode.HALF_UP);

        return new ExchangeRate(
                from.toUpperCase(),
                to.toUpperCase(),
                rate,
                LocalDateTime.now()
        );
    }
}
