package com.anupam.order.model;

import com.anupam.events.OrderItem;
import java.util.List;

/**
 * Request body for creating a new order.
 *
 * @param customerId the customer placing the order
 * @param items      list of line items in the order
 * @author Anupam
 */
public record CreateOrderRequest(String customerId, List<OrderItem> items) {}
