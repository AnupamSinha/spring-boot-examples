package com.anupam.shortener.controller;

import com.anupam.shortener.dto.ShortenRequest;
import com.anupam.shortener.dto.ShortenResponse;
import com.anupam.shortener.model.UrlMapping;
import com.anupam.shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Shortens a URL and returns the generated short link.
     */
    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shorten(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Redirects a short code to the original URL using 302 Found.
     * We use 302 (not 301) so browsers don't cache the redirect permanently,
     * allowing us to track clicks and handle expiration correctly.
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = urlService.resolve(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    /**
     * Returns analytics/stats for a given short code.
     */
    @GetMapping("/api/stats/{code}")
    public ResponseEntity<UrlMapping> stats(@PathVariable String code) {
        UrlMapping mapping = urlService.getStats(code);
        return ResponseEntity.ok(mapping);
    }
}
