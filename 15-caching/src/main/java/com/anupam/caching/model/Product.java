package com.anupam.caching.model;

import java.io.Serializable;
import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        BigDecimal price,
        String category
) implements Serializable {
}
