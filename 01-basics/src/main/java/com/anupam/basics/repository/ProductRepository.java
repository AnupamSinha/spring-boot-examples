package com.anupam.basics.repository;

import com.anupam.basics.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Product} entities.
 *
 * Extends JpaRepository to get standard CRUD operations out of the box.
 * Custom query methods follow Spring Data naming conventions for automatic implementation.
 *
 * @author Anupam
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all products belonging to a specific category.
     *
     * @param category the category name to filter by
     * @return list of products in the given category
     */
    List<Product> findByCategory(String category);
}
