package com.anupam.agents.tools;

import com.anupam.agents.model.FlightOption;
import com.anupam.agents.model.HotelOption;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Tools for the travel planning agent.
 *
 * The model chains these tools in a prescribed order to build a complete travel plan:
 * 1. Check weather -> 2. Search flights -> 3. Search hotels -> 4. Get activities -> 5. Calculate budget
 *
 * Each method returns simulated data for demonstration purposes.
 * In production, these would call real APIs (flight aggregators, hotel booking systems, etc.).
 *
 * @author Anupam
 */
@Component
public class TravelTools {

    private static final Map<String, String> WEATHER_DATA = Map.of(
            "paris", "Partly cloudy, 22°C. Light breeze. Perfect for sightseeing.",
            "tokyo", "Sunny, 28°C. Humid. Carry water and sunscreen.",
            "london", "Overcast, 16°C. Chance of light rain. Bring an umbrella.",
            "new york", "Clear skies, 25°C. Warm evenings. Great for outdoor dining.",
            "barcelona", "Sunny, 30°C. Hot afternoons. Best for beach and early morning walks.",
            "bali", "Tropical, 29°C. Brief afternoon showers. Ideal for surfing."
    );

    /** Gets the weather forecast to help with packing and activity planning. */
    @Tool(description = "Get the weather forecast for a city during the next week. " +
            "Use this to help decide what to pack and what activities are suitable.")
    public String getWeatherForecast(
            @ToolParam(description = "City name, e.g. Paris") String city) {

        String weather = WEATHER_DATA.get(city.toLowerCase());
        if (weather == null) {
            return "Weather data not available for " + city + ". Assume mild conditions.";
        }
        return weather;
    }

    /** Searches for available flights between two cities on a given date. */
    @Tool(description = "Search for available flights to a destination. " +
            "Returns a list of flight options with airlines, times, and prices.")
    public List<FlightOption> searchFlights(
            @ToolParam(description = "Departure city") String from,
            @ToolParam(description = "Destination city") String to,
            @ToolParam(description = "Travel date in YYYY-MM-DD format") String date) {

        return List.of(
                new FlightOption("Air France", from + " 08:00", to + " 11:30",
                        new BigDecimal("450.00"), "USD"),
                new FlightOption("Emirates", from + " 14:00", to + " 17:45",
                        new BigDecimal("620.00"), "USD"),
                new FlightOption("Budget Air", from + " 22:00", to + " 01:30+1",
                        new BigDecimal("280.00"), "USD")
        );
    }

    /** Searches for available hotels in a city for a given check-in date and duration. */
    @Tool(description = "Search for available hotels in a city. " +
            "Returns options with star rating, price per night, and guest rating.")
    public List<HotelOption> searchHotels(
            @ToolParam(description = "City name") String city,
            @ToolParam(description = "Check-in date in YYYY-MM-DD format") String checkIn,
            @ToolParam(description = "Number of nights") int nights) {

        return List.of(
                new HotelOption("Grand Palace Hotel", 5, new BigDecimal("320.00"), "USD", 4.8),
                new HotelOption("City Center Inn", 3, new BigDecimal("120.00"), "USD", 4.2),
                new HotelOption("Boutique Loft", 4, new BigDecimal("195.00"), "USD", 4.6),
                new HotelOption("Backpacker's Haven", 2, new BigDecimal("55.00"), "USD", 3.9)
        );
    }

    /** Gets recommended activities and attractions for a destination city. */
    @Tool(description = "Get recommended activities and attractions for a city. " +
            "Returns a curated list based on the weather and time of year.")
    public List<String> getActivities(
            @ToolParam(description = "City name") String city,
            @ToolParam(description = "Type: adventure, culture, food, relaxation", required = false) String type) {

        Map<String, List<String>> activities = Map.of(
                "paris", List.of("Visit the Louvre Museum", "Walk along the Seine at sunset",
                        "Explore Montmartre", "Try croissants at Du Pain et des Idées",
                        "Day trip to Versailles"),
                "tokyo", List.of("Visit Senso-ji Temple", "Explore Akihabara district",
                        "Try ramen at Ichiran", "Walk through Meiji Shrine gardens",
                        "Evening at Shibuya Crossing"),
                "london", List.of("British Museum (free entry)", "Walk along South Bank",
                        "Afternoon tea at The Ritz", "Explore Camden Market",
                        "Watch a West End show")
        );

        List<String> result = activities.get(city.toLowerCase());
        return result != null ? result : List.of(
                "Visit local museums", "Try local cuisine", "Walk the main streets",
                "Visit a local market", "Take a guided tour"
        );
    }

    /**
     * Calculates an estimated total trip budget with a detailed breakdown.
     * Assumes round-trip flights and per-day costs for food and activities.
     */
    @Tool(description = "Calculate the estimated total budget for a trip including flights, " +
            "hotel, food, and activities. Provides a breakdown.")
    public String calculateBudget(
            @ToolParam(description = "Flight cost (one way)") double flightCost,
            @ToolParam(description = "Hotel cost per night") double hotelPerNight,
            @ToolParam(description = "Number of nights") int nights,
            @ToolParam(description = "Daily food budget estimate") double dailyFood,
            @ToolParam(description = "Daily activities budget estimate") double dailyActivities) {

        double totalFlights = flightCost * 2; // round trip
        double totalHotel = hotelPerNight * nights;
        double totalFood = dailyFood * nights;
        double totalActivities = dailyActivities * nights;
        double total = totalFlights + totalHotel + totalFood + totalActivities;

        return String.format("""
                Budget Breakdown:
                - Flights (round trip): $%.2f
                - Hotel (%d nights): $%.2f
                - Food (%d days): $%.2f
                - Activities (%d days): $%.2f
                ─────────────────────────
                TOTAL: $%.2f USD
                """, totalFlights, nights, totalHotel, nights, totalFood,
                nights, totalActivities, total);
    }
}
