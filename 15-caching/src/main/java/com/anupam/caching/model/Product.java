package com.anupam.caching.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Product record used as a cached value object.
 *
 * Implements Serializable to support Redis cache serialization.
 * Immutable by virtue of being a Java record.
 *
 * @param id       unique identifier (null for new products)
 * @param name     product display name
 * @param price    product price
 * @param category product category
 * @author Anupam
 */
public record Product(
        Long id,
        String name,
        BigDecimal price,
        String category
) implements Serializable {
}
