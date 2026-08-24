package com.anupam.agents.model;

import java.math.BigDecimal;

public record FlightOption(
        String airline,
        String departure,
        String arrival,
        BigDecimal price,
        String currency
) {}
