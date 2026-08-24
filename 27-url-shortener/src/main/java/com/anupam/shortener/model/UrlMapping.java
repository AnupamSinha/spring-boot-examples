package com.anupam.shortener.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a URL mapping between a short code and the original URL.
 * <p>
 * Stores metadata including creation time, optional expiration, and click count
 * for analytics purposes. The short code column is uniquely indexed for fast lookups.
 * </p>
 *
 * @author Anupam
 */
@Entity
@Table(name = "url_mappings", indexes = {
        @Index(name = "idx_short_code", columnList = "shortCode", unique = true)
})
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Long clickCount = 0L;

    /**
     * JPA lifecycle callback that sets the creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Returns the auto-generated primary key.
     *
     * @return the entity ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity ID.
     *
     * @param id the entity ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the Base62-encoded short code.
     *
     * @return the short code
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * Sets the short code.
     *
     * @param shortCode the Base62-encoded short code
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * Returns the original long URL.
     *
     * @return the original URL
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    /**
     * Sets the original long URL.
     *
     * @param originalUrl the original URL to store
     */
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * Returns the timestamp when this mapping was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the optional expiration timestamp.
     *
     * @return the expiration time, or null if the URL does not expire
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the expiration timestamp.
     *
     * @param expiresAt the expiration time, or null for no expiration
     */
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Returns the number of times this short URL has been accessed.
     *
     * @return the click count
     */
    public Long getClickCount() {
        return clickCount;
    }

    /**
     * Sets the click count.
     *
     * @param clickCount the number of clicks
     */
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}
