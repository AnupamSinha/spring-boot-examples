package com.anupam.shortener.service;

import com.anupam.shortener.dto.ShortenRequest;
import com.anupam.shortener.dto.ShortenResponse;
import com.anupam.shortener.exception.UrlNotFoundException;
import com.anupam.shortener.model.UrlMapping;
import com.anupam.shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Core service for URL shortening, resolution, and analytics.
 * <p>
 * Handles the business logic of creating shortened URLs using Base62 encoding
 * of database IDs, resolving short codes back to original URLs with expiration
 * checks, and providing click statistics.
 * </p>
 *
 * @author Anupam
 */
@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final String baseUrl;

    /**
     * Constructs the URL service with its dependencies.
     *
     * @param urlRepository the repository for URL mapping persistence
     * @param base62Encoder the encoder for generating short codes
     * @param baseUrl       the application's base URL (e.g., http://localhost:8080)
     */
    public UrlService(UrlRepository urlRepository,
                      Base62Encoder base62Encoder,
                      @Value("${app.base-url}") String baseUrl) {
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.baseUrl = baseUrl;
    }

    /**
     * Shortens a URL by persisting it first (to get auto-generated ID),
     * then encoding the ID to Base62 as the short code.
     *
     * @param request the shorten request containing the URL and optional expiration
     * @return the response containing the generated short URL
     */
    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(request.url());
        mapping.setShortCode("temp"); // placeholder, updated after save

        // Set expiration if requested
        if (request.expiresInDays() != null && request.expiresInDays() > 0) {
            mapping.setExpiresAt(LocalDateTime.now().plusDays(request.expiresInDays()));
        }

        // Save to get the auto-generated ID
        mapping = urlRepository.save(mapping);

        // Encode the database ID to Base62 for a compact short code
        String shortCode = base62Encoder.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        mapping = urlRepository.save(mapping);

        String shortUrl = baseUrl + "/" + shortCode;
        return new ShortenResponse(shortUrl, mapping.getOriginalUrl(), mapping.getExpiresAt());
    }

    /**
     * Resolves a short code to the original URL. Increments the click count.
     * <p>
     * Throws {@link UrlNotFoundException} if the code doesn't exist or the URL has expired.
     * </p>
     *
     * @param shortCode the Base62-encoded short code to resolve
     * @return the original URL
     * @throws UrlNotFoundException if the short code is not found or has expired
     */
    @Transactional
    public String resolve(String shortCode) {
        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));

        // Check if the URL has expired
        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("Short URL has expired: " + shortCode);
        }

        // Track the click for analytics
        urlRepository.incrementClickCount(shortCode);
        return mapping.getOriginalUrl();
    }

    /**
     * Returns statistics for a short code including click count and metadata.
     *
     * @param shortCode the short code to look up
     * @return the URL mapping entity with all stats
     * @throws UrlNotFoundException if the short code is not found
     */
    @Transactional(readOnly = true)
    public UrlMapping getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));
    }
}
