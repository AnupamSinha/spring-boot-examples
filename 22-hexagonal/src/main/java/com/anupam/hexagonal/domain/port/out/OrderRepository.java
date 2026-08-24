package com.anupam.hexagonal.domain.port.out;

import com.anupam.hexagonal.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Output port — defines what the domain needs from persistence.
 * <p>
 * The domain defines this interface; the infrastructure layer (adapter) implements it.
 * This inversion of control allows the domain to remain independent of any
 * specific persistence technology (JPA, MongoDB, etc.).
 * </p>
 *
 * @author Anupam
 */
public interface OrderRepository {

    /**
     * Persists an order to the underlying storage.
     *
     * @param order the domain order to save
     * @return the saved order
     */
    Order save(Order order);

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId the order identifier
     * @return an Optional containing the order if found
     */
    Optional<Order> findById(String orderId);

    /**
     * Retrieves all orders belonging to a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of orders for the customer
     */
    List<Order> findByCustomerId(String customerId);
}
