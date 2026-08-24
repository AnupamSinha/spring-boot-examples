package com.anupam.jpa.service;

import com.anupam.jpa.model.Product;
import com.anupam.jpa.repository.ProductRepository;
import com.anupam.jpa.specification.ProductSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Builds a dynamic query by composing Specifications based on the provided filter parameters.
     * Null parameters are ignored — only non-null values contribute predicates.
     */
    public List<Product> findProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, String name) {
        Specification<Product> spec = Specification.where(null);

        if (category != null && !category.isBlank()) {
            spec = spec.and(ProductSpecifications.hasCategory(category));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(ProductSpecifications.priceBetween(minPrice, maxPrice));
        }

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecifications.nameContains(name));
        }

        return productRepository.findAll(spec);
    }
}
