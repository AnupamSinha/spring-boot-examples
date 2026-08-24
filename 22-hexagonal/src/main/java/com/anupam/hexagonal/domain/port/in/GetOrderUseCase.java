package com.anupam.hexagonal.domain.port.in;

import com.anupam.hexagonal.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Input port — defines the query operations the application supports for orders.
 * <p>
 * Implemented by the domain service and called by input adapters (e.g., controllers).
 * </p>
 *
 * @author Anupam
 */
public interface GetOrderUseCase {

    /**
     * Retrieves a single order by its unique identifier.
     *
     * @param orderId the order identifier
     * @return an Optional containing the order if found, empty otherwise
     */
    Optional<Order> getOrder(String orderId);

    /**
     * Retrieves all orders belonging to a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of orders for the customer (may be empty)
     */
    List<Order> getOrdersByCustomer(String customerId);
}
