package com.anupam.shortener.dto;

import java.time.LocalDateTime;

/**
 * Response DTO returned after successfully shortening a URL.
 * <p>
 * Contains the generated short URL, the original URL, and the optional
 * expiration timestamp.
 * </p>
 *
 * @param shortUrl    the generated short URL (base URL + short code)
 * @param originalUrl the original long URL
 * @param expiresAt   the expiration timestamp, or null if the URL does not expire
 * @author Anupam
 */
public record ShortenResponse(
        String shortUrl,
        String originalUrl,
        LocalDateTime expiresAt
) {
}
