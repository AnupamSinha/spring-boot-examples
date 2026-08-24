package com.anupam.fullstack.repository;

import com.anupam.fullstack.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Product} entities.
 * <p>
 * Provides standard CRUD operations inherited from {@link JpaRepository}.
 * </p>
 *
 * @author Anupam
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
