package com.anupam.loadtest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public ProductController() {
        for (int i = 1; i <= 50; i++) {
            long id = idGenerator.incrementAndGet();
            products.put(id, new Product(id, "Product " + id,
                    BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10, 500)).setScale(2, java.math.RoundingMode.HALF_UP),
                    "Description for product " + id));
        }
    }

    @GetMapping
    public List<Product> getAll() throws InterruptedException {
        // Simulate some processing time
        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
        return List.copyOf(products.values());
    }

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

    public record Product(Long id, String name, BigDecimal price, String description) {}
    public record CreateProductRequest(String name, BigDecimal price, String description) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
