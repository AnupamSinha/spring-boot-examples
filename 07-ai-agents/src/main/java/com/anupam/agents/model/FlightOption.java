package com.anupam.agents.model;

import java.math.BigDecimal;

/**
 * Represents a single flight search result.
 *
 * @param airline   the airline name
 * @param departure departure time and city
 * @param arrival   arrival time and city
 * @param price     ticket price
 * @param currency  currency code for the price
 * @author Anupam
 */
public record FlightOption(
        String airline,
        String departure,
        String arrival,
        BigDecimal price,
        String currency
) {}
