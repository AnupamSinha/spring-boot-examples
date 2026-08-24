package com.anupam.performance.repository;

import com.anupam.performance.model.Product;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product repository with Hibernate query hints for performance optimization.
 *
 * Uses @QueryHints to:
 * - Mark read-only queries (avoids dirty-checking overhead in the persistence context)
 * - Enable second-level caching for frequently accessed data
 * - Control JDBC fetch size for batch loading
 *
 * @author Anupam
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products by category with read-only and cacheable hints.
     * Read-only avoids Hibernate's dirty checking; cacheable enables L2 cache.
     */
    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true"),
            @QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true")
    })
    List<Product> findByCategory(String category);

    /**
     * Retrieves all products with read-only mode and a fetch size of 50.
     * The fetch size hint reduces JDBC round-trips for large result sets.
     */
    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true"),
            @QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "50")
    })
    List<Product> findAll();

    /**
     * Case-insensitive search by name substring, optimized as read-only.
     */
    @QueryHints({
            @QueryHint(name = HibernateHints.HINT_READ_ONLY, value = "true")
    })
    List<Product> findByNameContainingIgnoreCase(String name);
}
