package com.anupam.hexagonal.domain.port.in;

import com.anupam.hexagonal.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Input port — defines query operations the application supports.
 */
public interface GetOrderUseCase {

    Optional<Order> getOrder(String orderId);

    List<Order> getOrdersByCustomer(String customerId);
}
