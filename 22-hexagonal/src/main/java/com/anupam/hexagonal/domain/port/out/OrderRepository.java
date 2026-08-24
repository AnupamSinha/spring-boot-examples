package com.anupam.hexagonal.domain.port.out;

import com.anupam.hexagonal.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Output port — defines what the domain needs from persistence.
 * The domain defines this interface; the infrastructure implements it.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(String orderId);

    List<Order> findByCustomerId(String customerId);
}
