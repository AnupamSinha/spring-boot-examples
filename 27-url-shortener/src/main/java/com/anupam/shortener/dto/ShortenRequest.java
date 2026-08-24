package com.anupam.shortener.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for the URL shortening endpoint.
 * <p>
 * Contains the original URL to shorten and an optional expiration period.
 * </p>
 *
 * @param url           the original URL to shorten (must not be blank)
 * @param expiresInDays optional number of days until the short URL expires
 * @author Anupam
 */
public record ShortenRequest(
        @NotBlank(message = "URL must not be blank")
        String url,
        Integer expiresInDays
) {
}
