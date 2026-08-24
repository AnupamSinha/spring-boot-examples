package com.anupam.shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Shell commands for HTTP operations and network connectivity testing.
 * <p>
 * Provides a simple HTTP GET client and a ping command to check host reachability.
 * </p>
 *
 * @author Anupam
 */
@ShellComponent
public class HttpCommands {

    /** Shared HTTP client with a 10-second connection timeout. */
    private final HttpClient httpClient;

    /**
     * Initializes the HTTP client with a default connection timeout.
     */
    public HttpCommands() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Performs an HTTP GET request to the specified URL and returns the response.
     * <p>
     * By default, returns the status code and response body (truncated to 2000 characters).
     * When {@code headersOnly} is true, only the status and response headers are shown.
     * </p>
     *
     * @param url         the target URL for the GET request
     * @param headersOnly if true, only display response headers instead of the body
     * @return the formatted HTTP response output
     * @throws IOException          if an I/O error occurs during the request
     * @throws InterruptedException if the request is interrupted
     */
    @ShellMethod(key = "http-get", value = "Perform an HTTP GET request")
    public String httpGet(String url,
                          @ShellOption(defaultValue = "false") boolean headersOnly)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        StringBuilder result = new StringBuilder();
        result.append("Status: ").append(response.statusCode()).append("\n");

        if (headersOnly) {
            // Display all response headers
            response.headers().map().forEach((key, values) ->
                    values.forEach(value ->
                            result.append(key).append(": ").append(value).append("\n")
                    ));
        } else {
            // Display the response body, truncated for readability
            result.append("\n").append(truncate(response.body(), 2000));
        }

        return result.toString();
    }

    /**
     * Pings a host to check network connectivity.
     * <p>
     * Attempts to reach the specified host within the given timeout and reports
     * the result along with round-trip time.
     * </p>
     *
     * @param host    the hostname or IP address to ping
     * @param timeout the timeout in milliseconds; defaults to 5000
     * @return a message indicating whether the host is reachable and the elapsed time
     */
    @ShellMethod(key = "ping", value = "Ping a host to check connectivity")
    public String ping(String host,
                       @ShellOption(defaultValue = "5000") int timeout) {
        try {
            long start = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(host);
            boolean reachable = address.isReachable(timeout);
            long elapsed = System.currentTimeMillis() - start;

            if (reachable) {
                return "Host %s (%s) is reachable — %d ms".formatted(
                        host, address.getHostAddress(), elapsed);
            } else {
                return "Host %s is not reachable (timeout: %d ms)".formatted(host, timeout);
            }
        } catch (IOException e) {
            return "Failed to ping %s: %s".formatted(host, e.getMessage());
        }
    }

    /**
     * Truncates a string to the specified maximum length, appending an indicator if truncated.
     *
     * @param text      the text to truncate
     * @param maxLength the maximum allowed length
     * @return the original text if within limits, or a truncated version with a notice
     */
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "\n... (truncated)";
    }
}
