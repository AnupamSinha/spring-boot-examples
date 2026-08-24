package com.anupam.multitenant.repository;

import com.anupam.multitenant.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Product} entities.
 * <p>
 * Provides standard CRUD operations. All queries are automatically scoped to the
 * current tenant's database schema via the routing data source.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
