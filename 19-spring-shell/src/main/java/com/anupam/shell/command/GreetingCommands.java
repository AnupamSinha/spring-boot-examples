package com.anupam.shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * General utility shell commands for greetings, date display, and version information.
 * <p>
 * These commands demonstrate basic Spring Shell capabilities with parameter handling
 * and option defaults.
 * </p>
 *
 * @author Anupam
 */
@ShellComponent
public class GreetingCommands {

    /** Application version string. */
    private static final String VERSION = "1.0.0";

    /**
     * Generates a personalized greeting message.
     *
     * @param name      the name to greet; defaults to "World"
     * @param uppercase if true, the greeting is returned in uppercase
     * @return the formatted greeting string
     */
    @ShellMethod(key = "hello", value = "Say hello")
    public String hello(
            @ShellOption(defaultValue = "World") String name,
            @ShellOption(defaultValue = "false") boolean uppercase) {

        String greeting = "Hello, %s! Welcome to Spring Shell CLI.".formatted(name);
        return uppercase ? greeting.toUpperCase() : greeting;
    }

    /**
     * Displays the current date and time in "yyyy-MM-dd HH:mm:ss" format.
     *
     * @return the formatted current date and time
     */
    @ShellMethod(key = "date", value = "Display current date and time")
    public String date() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Displays application version along with Java and OS information.
     *
     * @return a multi-line string with version and environment details
     */
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
