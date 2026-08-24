package com.anupam.native_app.controller;

import com.anupam.native_app.listener.StartupTimeListener;
import com.anupam.native_app.model.Greeting;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * REST controller demonstrating GraalVM native image capabilities.
 *
 * Provides endpoints to:
 * - Return a greeting (proves the app runs as native binary)
 * - Demonstrate reflection working in a native image
 * - Report application startup time (showcases near-instant boot)
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class GreetingController {

    private final StartupTimeListener startupTimeListener;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public GreetingController(StartupTimeListener startupTimeListener) {
        this.startupTimeListener = startupTimeListener;
    }

    /** Returns a greeting with the current timestamp and active profile. */
    @GetMapping("/greet")
    public Greeting greet() {
        return new Greeting(
                "Hello from Spring Boot Native!",
                Instant.now().toString(),
                activeProfile
        );
    }

    /**
     * Demonstrates that reflection works correctly in the native image.
     * Uses @RegisterReflectionForBinding to ensure the Greeting class
     * metadata is available at runtime.
     */
    @GetMapping("/reflect")
    @RegisterReflectionForBinding(Greeting.class)
    public Map<String, Object> reflect() {
        var clazz = Greeting.class;
        return Map.of(
                "className", clazz.getName(),
                "recordComponents", clazz.getRecordComponents().length,
                "fields", java.util.Arrays.stream(clazz.getDeclaredFields())
                        .map(java.lang.reflect.Field::getName)
                        .toList(),
                "message", "Reflection works in native image thanks to @RegisterReflectionForBinding"
        );
    }

    /**
     * Reports the application startup duration.
     * Native images typically start in tens of milliseconds vs. seconds for JVM mode.
     */
    @GetMapping("/startup")
    public Map<String, Object> startup() {
        var duration = startupTimeListener.getStartupDuration();
        return Map.of(
                "startupTimeMs", duration != null ? duration.toMillis() : -1,
                "startupTime", duration != null ? duration.toString() : "unknown",
                "message", "Application startup duration measured via ApplicationStartedEvent"
        );
    }
}
