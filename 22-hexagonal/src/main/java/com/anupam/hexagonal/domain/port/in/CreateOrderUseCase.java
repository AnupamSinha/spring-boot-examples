package com.anupam.hexagonal.domain.port.in;

import com.anupam.hexagonal.domain.model.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Input port — defines what the application can do.
 * Implemented by domain service, called by adapters (controllers).
 */
public interface CreateOrderUseCase {

    Order createOrder(CreateOrderCommand command);

    record CreateOrderCommand(
        String customerId,
        List<OrderItemCommand> items
    ) {
        public record OrderItemCommand(
            String productId,
            String productName,
            int quantity,
            BigDecimal unitPrice
        ) {}
    }
}
