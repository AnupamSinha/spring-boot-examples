package com.anupam.shortener.repository;

import com.anupam.shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UrlMapping} entities.
 * <p>
 * Provides CRUD operations and custom queries for short code lookup
 * and click count increment.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

    /**
     * Finds a URL mapping by its unique short code.
     *
     * @param shortCode the Base62-encoded short code
     * @return an Optional containing the mapping if found
     */
    Optional<UrlMapping> findByShortCode(String shortCode);

    /**
     * Atomically increments the click count for a given short code.
     * <p>
     * Uses a JPQL update query to avoid read-modify-write race conditions.
     * </p>
     *
     * @param shortCode the short code whose click count to increment
     */
    @Modifying
    @Transactional
    @Query("UPDATE UrlMapping u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    void incrementClickCount(@Param("shortCode") String shortCode);
}
