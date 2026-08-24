package com.anupam.graphql.repository;

import com.anupam.graphql.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Product} entities.
 * <p>
 * Provides standard CRUD operations and derived query methods
 * for category-based and name-based lookups.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all products in the specified category.
     *
     * @param category the category to filter by
     * @return list of matching products
     */
    List<Product> findByCategory(String category);

    /**
     * Searches for products whose name contains the given string (case-insensitive).
     *
     * @param name the search term
     * @return list of matching products
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
