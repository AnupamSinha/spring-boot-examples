package com.anupam.basics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource (e.g., product) is not found.
 *
 * Annotated with @ResponseStatus so Spring automatically returns a 404 response
 * even if this exception is not explicitly caught by a controller advice.
 *
 * @author Anupam
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
