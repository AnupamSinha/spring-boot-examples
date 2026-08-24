package com.anupam.shortener.dto;

import java.time.LocalDateTime;

public record ShortenResponse(
        String shortUrl,
        String originalUrl,
        LocalDateTime expiresAt
) {
}
