package com.anupam.jpa.model;

import java.math.BigDecimal;

/**
 * Interface-based projection for products.
 *
 * Only fetches id, name, and price columns from the database,
 * reducing memory usage and network transfer for read-only list views
 * where the full entity is not needed.
 *
 * @author Anupam
 */
public interface ProductProjection {

    Long getId();

    String getName();

    BigDecimal getPrice();
}
