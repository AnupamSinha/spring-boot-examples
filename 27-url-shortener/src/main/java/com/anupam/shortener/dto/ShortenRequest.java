package com.anupam.shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenRequest(
        @NotBlank(message = "URL must not be blank")
        String url,
        Integer expiresInDays
) {
}
