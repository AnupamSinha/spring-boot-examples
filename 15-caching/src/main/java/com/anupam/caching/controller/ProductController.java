package com.anupam.caching.controller;

import com.anupam.caching.model.Product;
import com.anupam.caching.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product CRUD operations with caching.
 *
 * Response times will vary significantly between cache hits (~1ms)
 * and cache misses (~200ms) due to the simulated database delay.
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

    /** GET /api/products/{id} - Returns a product (cached after first access). */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /** GET /api/products - Returns all products (cached as a collection). */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    /** POST /api/products - Creates a product and updates the cache. */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(201).body(saved);
    }

    /** PUT /api/products/{id} - Updates a product and refreshes its cache entry. */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.update(id, product);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/products/{id} - Deletes a product and evicts it from cache. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
