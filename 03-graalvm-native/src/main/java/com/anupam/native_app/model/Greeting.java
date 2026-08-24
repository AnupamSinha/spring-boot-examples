package com.anupam.native_app.model;

/**
 * Immutable data carrier for greeting responses.
 *
 * Uses a Java record for concise, immutable value semantics.
 * Requires runtime hints for GraalVM native image serialization.
 *
 * @param message   the greeting text
 * @param timestamp the ISO-8601 timestamp when the greeting was generated
 * @param profile   the active Spring profile (e.g., "default", "prod")
 * @author Anupam
 */
public record Greeting(String message, String timestamp, String profile) {
}
