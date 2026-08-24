package com.anupam.agents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI Agents Demo - Main Application Entry Point.
 *
 * Demonstrates agentic AI patterns where the LLM autonomously plans
 * and executes a multi-step workflow (weather check, flight search,
 * hotel search, activity recommendations, budget calculation) to
 * build a complete travel plan.
 *
 * @author Anupam
 */
@SpringBootApplication
public class SpringAiAgentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiAgentsApplication.class, args);
    }
}
