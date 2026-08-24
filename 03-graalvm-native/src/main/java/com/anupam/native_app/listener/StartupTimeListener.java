package com.anupam.native_app.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StartupTimeListener {

    private Duration startupDuration;

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        this.startupDuration = event.getTimeTaken();
    }

    public Duration getStartupDuration() {
        return startupDuration;
    }
}
