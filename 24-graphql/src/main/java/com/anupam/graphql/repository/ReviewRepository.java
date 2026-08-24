package com.anupam.graphql.repository;

import com.anupam.graphql.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Review} entities.
 * <p>
 * Provides methods for querying reviews by product, including a batch query
 * method ({@link #findByProductIdIn}) used by the GraphQL batch mapping
 * to avoid the N+1 problem.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds all reviews for a specific product.
     *
     * @param productId the product ID
     * @return list of reviews for the product
     */
    List<Review> findByProductId(Long productId);

    /**
     * Batch query: finds all reviews for a list of product IDs in a single query.
     * Used by the GraphQL batch mapping to prevent N+1 queries.
     *
     * @param productIds the list of product IDs
     * @return list of all reviews belonging to any of the given products
     */
    List<Review> findByProductIdIn(List<Long> productIds);
}
