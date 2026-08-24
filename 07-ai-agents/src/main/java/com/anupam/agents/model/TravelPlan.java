package com.anupam.agents.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Structured output from the travel planning agent.
 * Aggregates all information gathered across multiple tool calls.
 *
 * @param destination    the target city
 * @param startDate      trip start date
 * @param endDate        trip end date
 * @param weather        weather forecast summary
 * @param activities     recommended activities list
 * @param flights        available flight options
 * @param hotels         available hotel options
 * @param budgetEstimate total estimated budget string
 * @author Anupam
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
