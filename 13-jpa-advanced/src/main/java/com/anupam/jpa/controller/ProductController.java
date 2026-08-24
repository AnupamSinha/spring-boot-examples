package com.anupam.jpa.controller;

import com.anupam.jpa.model.Product;
import com.anupam.jpa.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for querying products with dynamic filters.
 *
 * Supports optional query parameters that are combined into a
 * Specification-based query. Omitted parameters are ignored.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /api/products - Finds products matching optional filters.
     *
     * @param category category filter (exact match)
     * @param minPrice minimum price filter
     * @param maxPrice maximum price filter
     * @param name     name keyword filter (case-insensitive, partial match)
     * @return filtered list of products
     */
    @GetMapping
    public List<Product> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String name) {
        return productService.findProducts(category, minPrice, maxPrice, name);
    }
}
