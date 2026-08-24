package com.anupam.loadtest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * REST controller exposing product CRUD endpoints designed as the target
 * for Gatling load testing simulations. Includes artificial latency to
 * simulate realistic response times for performance benchmarking.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /** Thread-safe in-memory product store. */
    private final Map<Long, Product> products = new ConcurrentHashMap<>();

    /** Atomic counter for generating unique product IDs. */
    private final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * Initializes the controller with 50 pre-generated products
     * with random prices to simulate an existing product catalog.
     */
    public ProductController() {
        // Seed 50 products with random prices for load testing scenarios
        for (int i = 1; i <= 50; i++) {
            long id = idGenerator.incrementAndGet();
            products.put(id, new Product(id, "Product " + id,
                    BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10, 500)).setScale(2, java.math.RoundingMode.HALF_UP),
                    "Description for product " + id));
        }
    }

    /**
     * Retrieves all products from the in-memory store.
     * Includes a simulated processing delay (10-50ms) to mimic real database queries.
     *
     * @return the list of all available products
     * @throws InterruptedException if the simulated delay is interrupted
     */
    @GetMapping
    public List<Product> getAll() throws InterruptedException {
        // Simulate some processing time
        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
        return List.copyOf(products.values());
    }

    /**
     * Retrieves a single product by its ID.
     * Includes a simulated lookup delay (5-30ms) to mimic database access.
     *
     * @param id the unique product identifier
     * @return the product matching the given ID
     * @throws InterruptedException     if the simulated delay is interrupted
     * @throws ProductNotFoundException if no product exists with the given ID
     */
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) throws InterruptedException {
        // Simulate database lookup
        Thread.sleep(ThreadLocalRandom.current().nextInt(5, 30));
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + id);
        }
        return product;
    }

    /**
     * Creates a new product from the given request body.
     * Includes a heavier simulated delay (50-150ms) to mimic validation and database write.
     *
     * @param request the product creation request containing name, price, and description
     * @return the newly created product with a generated ID
     * @throws InterruptedException if the simulated delay is interrupted
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody CreateProductRequest request) throws InterruptedException {
        // Simulate heavier processing (validation, DB write)
        Thread.sleep(ThreadLocalRandom.current().nextInt(50, 150));
        long id = idGenerator.incrementAndGet();
        Product product = new Product(id, request.name(), request.price(), request.description());
        products.put(id, product);
        return product;
    }

    /**
     * Immutable record representing a product entity.
     *
     * @param id          the unique product identifier
     * @param name        the product name
     * @param price       the product price
     * @param description the product description
     */
    public record Product(Long id, String name, BigDecimal price, String description) {}

    /**
     * Immutable record representing a product creation request.
     *
     * @param name        the desired product name
     * @param price       the product price
     * @param description the product description
     */
    public record CreateProductRequest(String name, BigDecimal price, String description) {}

    /**
     * Exception thrown when a requested product is not found in the store.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ProductNotFoundException extends RuntimeException {

        /**
         * Constructs a ProductNotFoundException with the given message.
         *
         * @param message the detail message explaining which product was not found
         */
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
