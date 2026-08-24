package com.anupam.jpa.model;

import java.math.BigDecimal;

/**
 * Interface-based projection — only fetches id, name, and price columns.
 */
public interface ProductProjection {

    Long getId();

    String getName();

    BigDecimal getPrice();
}
