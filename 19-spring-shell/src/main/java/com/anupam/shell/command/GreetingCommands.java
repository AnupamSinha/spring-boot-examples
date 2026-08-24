package com.anupam.shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * General utility commands: greeting, date, and version.
 */
@ShellComponent
public class GreetingCommands {

    private static final String VERSION = "1.0.0";

    @ShellMethod(key = "hello", value = "Say hello")
    public String hello(
            @ShellOption(defaultValue = "World") String name,
            @ShellOption(defaultValue = "false") boolean uppercase) {

        String greeting = "Hello, %s! Welcome to Spring Shell CLI.".formatted(name);
        return uppercase ? greeting.toUpperCase() : greeting;
    }

    @ShellMethod(key = "date", value = "Display current date and time")
    public String date() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @ShellMethod(key = "version", value = "Display application version")
    public String version() {
        return """
                Spring Shell CLI v%s
                Java %s
                OS: %s %s
                """.formatted(
                VERSION,
                System.getProperty("java.version"),
                System.getProperty("os.name"),
                System.getProperty("os.arch")
        );
    }
}
