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

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final String baseUrl;

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
     */
    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(request.url());
        mapping.setShortCode("temp"); // placeholder, updated after save

        if (request.expiresInDays() != null && request.expiresInDays() > 0) {
            mapping.setExpiresAt(LocalDateTime.now().plusDays(request.expiresInDays()));
        }

        // Save to get the auto-generated ID
        mapping = urlRepository.save(mapping);

        // Encode ID to Base62 for the short code
        String shortCode = base62Encoder.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        mapping = urlRepository.save(mapping);

        String shortUrl = baseUrl + "/" + shortCode;
        return new ShortenResponse(shortUrl, mapping.getOriginalUrl(), mapping.getExpiresAt());
    }

    /**
     * Resolves a short code to the original URL. Increments click count.
     * Throws UrlNotFoundException if code doesn't exist or URL is expired.
     */
    @Transactional
    public String resolve(String shortCode) {
        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));

        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("Short URL has expired: " + shortCode);
        }

        urlRepository.incrementClickCount(shortCode);
        return mapping.getOriginalUrl();
    }

    /**
     * Returns stats for a short code including click count and metadata.
     */
    @Transactional(readOnly = true)
    public UrlMapping getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));
    }
}
