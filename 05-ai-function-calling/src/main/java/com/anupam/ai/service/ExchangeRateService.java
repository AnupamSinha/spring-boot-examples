package com.anupam.ai.service;

import com.anupam.ai.model.ExchangeRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Simulated exchange rate service.
 * In production, this would call an external API like exchangeratesapi.io.
 */
@Service
public class ExchangeRateService {

    private static final Map<String, BigDecimal> RATES_TO_USD = Map.of(
            "USD", BigDecimal.ONE,
            "EUR", new BigDecimal("0.92"),
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("149.50"),
            "INR", new BigDecimal("83.12"),
            "CAD", new BigDecimal("1.36"),
            "AUD", new BigDecimal("1.53")
    );

    public ExchangeRate getRate(String from, String to) {
        BigDecimal fromToUsd = RATES_TO_USD.get(from.toUpperCase());
        BigDecimal toToUsd = RATES_TO_USD.get(to.toUpperCase());

        if (fromToUsd == null || toToUsd == null) {
            throw new IllegalArgumentException(
                    "Unsupported currency. Supported: " + RATES_TO_USD.keySet());
        }

        // Convert: from -> USD -> to
        BigDecimal rate = toToUsd.divide(fromToUsd, 6, RoundingMode.HALF_UP);

        return new ExchangeRate(
                from.toUpperCase(),
                to.toUpperCase(),
                rate,
                LocalDateTime.now()
        );
    }
}
