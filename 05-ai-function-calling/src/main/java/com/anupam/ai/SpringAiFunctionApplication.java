package com.anupam.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI Function Calling Demo - Main Application Entry Point.
 *
 * Demonstrates Spring AI's function calling (tool use) capability where an LLM
 * can invoke Java methods at runtime to fetch live data (payment status,
 * exchange rates) before composing its response.
 *
 * @author Anupam
 */
@SpringBootApplication
public class SpringAiFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiFunctionApplication.class, args);
    }
}
