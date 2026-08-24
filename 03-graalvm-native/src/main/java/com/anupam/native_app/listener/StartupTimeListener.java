package com.anupam.native_app.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Captures application startup duration by listening to the ApplicationStartedEvent.
 *
 * This is particularly useful for comparing startup times between
 * JVM mode and GraalVM native image mode.
 *
 * @author Anupam
 */
@Component
public class StartupTimeListener {

    private Duration startupDuration;

    /**
     * Invoked by Spring when the application has fully started.
     * Stores the time taken from process start to application-ready state.
     */
    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        this.startupDuration = event.getTimeTaken();
    }

    /**
     * Returns the measured startup duration, or null if the event hasn't fired yet.
     */
    public Duration getStartupDuration() {
        return startupDuration;
    }
}
