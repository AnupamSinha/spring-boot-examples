package com.anupam.agents.model;

import java.math.BigDecimal;

/**
 * Represents a single hotel search result.
 *
 * @param name          hotel name
 * @param stars         star rating (1-5)
 * @param pricePerNight nightly rate
 * @param currency      currency code for the price
 * @param rating        guest rating (0.0-5.0)
 * @author Anupam
 */
public record HotelOption(
        String name,
        int stars,
        BigDecimal pricePerNight,
        String currency,
        double rating
) {}
