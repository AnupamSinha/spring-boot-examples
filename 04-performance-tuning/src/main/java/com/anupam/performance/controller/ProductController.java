package com.anupam.performance.controller;

import com.anupam.performance.model.Product;
import com.anupam.performance.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for product operations with cached and async variants.
 *
 * Exposes endpoints that demonstrate the performance difference between:
 * - Cached vs. uncached data access
 * - Synchronous vs. asynchronous query execution
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

    /** GET /api/products - Returns all products (cached). */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /** GET /api/products/{id} - Returns a product by ID (cached per entity). */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/products/category/{category} - Returns products filtered by category (cached). */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    /** GET /api/products/uncached - Returns all products bypassing the cache (for benchmarking). */
    @GetMapping("/uncached")
    public ResponseEntity<List<Product>> getAllProductsUncached() {
        return ResponseEntity.ok(productService.getProductsUncached());
    }

    /** GET /api/products/async - Returns all products asynchronously via CompletableFuture. */
    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<Product>>> getAllProductsAsync() {
        return productService.getAllProductsAsync()
                .thenApply(ResponseEntity::ok);
    }

    /** GET /api/products/search?name=... - Async name search with partial matching. */
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<Product>>> searchProducts(@RequestParam String name) {
        return productService.searchProductsAsync(name)
                .thenApply(ResponseEntity::ok);
    }

    /** POST /api/products - Creates a product and invalidates caches. */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    /** DELETE /api/products/{id} - Deletes a product and invalidates caches. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
