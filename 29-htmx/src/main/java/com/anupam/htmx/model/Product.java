package com.anupam.htmx.model;

import java.math.BigDecimal;

/**
 * Immutable product record representing an item in the catalog.
 *
 * @param id    the unique product identifier
 * @param name  the product display name
 * @param price the product price
 * @author Anupam
 */
public record Product(Long id, String name, BigDecimal price) {
}
