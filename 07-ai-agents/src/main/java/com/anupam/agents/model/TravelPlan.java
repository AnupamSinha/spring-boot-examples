package com.anupam.agents.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Structured output from the travel planning agent.
 */
public record TravelPlan(
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        String weather,
        List<String> activities,
        List<FlightOption> flights,
        List<HotelOption> hotels,
        String budgetEstimate
) {}
