package com.anupam.hexagonal.domain.port.in;

import com.anupam.hexagonal.domain.model.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Input port — defines the order creation capability exposed by the application.
 * <p>
 * Implemented by the domain service and called by input adapters (e.g., controllers).
 * This interface keeps the adapter layer decoupled from the service implementation.
 * </p>
 *
 * @author Anupam
 */
public interface CreateOrderUseCase {

    /**
     * Creates a new order based on the provided command data.
     *
     * @param command the command containing customer and item details
     * @return the created domain Order (with generated ID and confirmed status)
     */
    Order createOrder(CreateOrderCommand command);

    /**
     * Command object encapsulating the data required to create an order.
     *
     * @param customerId the customer placing the order
     * @param items      the list of items to include in the order
     */
    record CreateOrderCommand(
        String customerId,
        List<OrderItemCommand> items
    ) {
        /**
         * Represents a single item within the order creation command.
         *
         * @param productId   the product identifier
         * @param productName the human-readable product name
         * @param quantity    the quantity to order
         * @param unitPrice   the price per unit
         */
        public record OrderItemCommand(
            String productId,
            String productName,
            int quantity,
            BigDecimal unitPrice
        ) {}
    }
}
