package com.anupam.agents.model;

import java.math.BigDecimal;

public record HotelOption(
        String name,
        int stars,
        BigDecimal pricePerNight,
        String currency,
        double rating
) {}
