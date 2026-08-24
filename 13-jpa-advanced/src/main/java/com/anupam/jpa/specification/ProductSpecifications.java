package com.anupam.jpa.specification;

import com.anupam.jpa.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable, composable JPA Specifications for dynamic Product queries.
 *
 * Each static method returns a Specification that can be combined using
 * .and() / .or() to build complex queries without string concatenation
 * or conditional JPQL.
 *
 * @author Anupam
 */
public final class ProductSpecifications {

    private ProductSpecifications() {
        // Utility class - no instantiation
    }

    /** Filters products by exact category match. */
    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    /** Filters products with price between min and max (inclusive). */
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> cb.between(root.get("price"), min, max);
    }

    /** Filters products whose name contains the keyword (case-insensitive). */
    public static Specification<Product> nameContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
}
