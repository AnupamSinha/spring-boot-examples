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
 * HTTP-related shell commands: simple GET requests and ping.
 */
@ShellComponent
public class HttpCommands {

    private final HttpClient httpClient;

    public HttpCommands() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

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
            response.headers().map().forEach((key, values) ->
                    values.forEach(value ->
                            result.append(key).append(": ").append(value).append("\n")
                    ));
        } else {
            result.append("\n").append(truncate(response.body(), 2000));
        }

        return result.toString();
    }

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

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "\n... (truncated)";
    }
}
