package com.anupam.shortener.exception;

/**
 * Exception thrown when a requested short code cannot be found
 * or the associated URL has expired.
 * <p>
 * Results in an HTTP 404 Not Found response.
 * </p>
 *
 * @author Anupam
 */
public class UrlNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining which short code was not found
     */
    public UrlNotFoundException(String message) {
        super(message);
    }
}
