package com.anupam.outbox.repository;

import com.anupam.outbox.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Order} entities.
 * <p>
 * Provides standard CRUD operations for the orders table.
 * </p>
 *
 * @author Anupam
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
