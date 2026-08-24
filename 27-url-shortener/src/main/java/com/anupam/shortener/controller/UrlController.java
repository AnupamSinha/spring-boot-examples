package com.anupam.shortener.controller;

import com.anupam.shortener.dto.ShortenRequest;
import com.anupam.shortener.dto.ShortenResponse;
import com.anupam.shortener.model.UrlMapping;
import com.anupam.shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST controller for the URL Shortener service.
 * <p>
 * Exposes endpoints for creating short URLs, redirecting short codes
 * to original URLs, and retrieving click analytics.
 * </p>
 *
 * @author Anupam
 */
@RestController
public class UrlController {

    private final UrlService urlService;

    /**
     * Constructs the controller with the URL service dependency.
     *
     * @param urlService the service handling URL shortening logic
     */
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Shortens a URL and returns the generated short link.
     *
     * @param request the request body containing the URL to shorten and optional expiration
     * @return a 201 Created response with the shortened URL details
     */
    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shorten(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Redirects a short code to the original URL using 302 Found.
     * <p>
     * Uses 302 (not 301) so browsers don't cache the redirect permanently,
     * allowing click tracking and expiration handling to work correctly.
     * </p>
     *
     * @param code the short code to resolve
     * @return a 302 redirect response to the original URL
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = urlService.resolve(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    /**
     * Returns analytics and statistics for a given short code.
     *
     * @param code the short code to look up stats for
     * @return the URL mapping with click count and metadata
     */
    @GetMapping("/api/stats/{code}")
    public ResponseEntity<UrlMapping> stats(@PathVariable String code) {
        UrlMapping mapping = urlService.getStats(code);
        return ResponseEntity.ok(mapping);
    }
}
