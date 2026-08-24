package com.anupam.cqrs.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Command object representing a request to create a new order.
 * <p>
 * In a CQRS architecture, commands encapsulate the intent to mutate state.
 * This record carries the customer identifier and a list of order items
 * that are validated before processing.
 * </p>
 *
 * @param customerId the unique identifier of the customer placing the order
 * @param items      the list of items included in the order (must not be empty)
 *
 * @author Anupam
 */
public record CreateOrderCommand(
    @NotBlank String customerId,
    @NotEmpty @Valid List<OrderItem> items
) {
    /**
     * Represents a single line item within an order command.
     *
     * @param productId the unique identifier of the product
     * @param quantity  the number of units ordered
     * @param price     the unit price of the product
     */
    public record OrderItem(
        @NotBlank String productId,
        int quantity,
        BigDecimal price
    ) {}
}
