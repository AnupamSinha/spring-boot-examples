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

@RestController
@RequestMapping("/api")
public class GreetingController {

    private final StartupTimeListener startupTimeListener;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public GreetingController(StartupTimeListener startupTimeListener) {
        this.startupTimeListener = startupTimeListener;
    }

    @GetMapping("/greet")
    public Greeting greet() {
        return new Greeting(
                "Hello from Spring Boot Native!",
                Instant.now().toString(),
                activeProfile
        );
    }

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
