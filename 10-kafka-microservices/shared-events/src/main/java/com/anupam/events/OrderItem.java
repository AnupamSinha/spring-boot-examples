package com.anupam.events;

import java.math.BigDecimal;

/**
 * Represents a single item within an order.
 *
 * @param productId unique product identifier
 * @param name      product display name
 * @param quantity  number of units ordered
 * @param price     unit price
 * @author Anupam
 */
public record OrderItem(String productId, String name, int quantity, BigDecimal price) {}
