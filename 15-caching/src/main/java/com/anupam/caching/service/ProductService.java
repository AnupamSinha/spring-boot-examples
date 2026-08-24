package com.anupam.caching.service;

import com.anupam.caching.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Product service demonstrating Spring Cache annotations.
 *
 * Uses an in-memory map as a simulated database with 200ms artificial latency
 * to make cache hits vs. misses clearly visible in response times.
 *
 * Annotations used:
 * - @Cacheable: read-through (populate cache on miss)
 * - @CachePut: write-through (update cache on write)
 * - @CacheEvict: invalidation (remove stale entries)
 *
 * @author Anupam
 */
@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    // Simulated database
    private final Map<Long, Product> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public ProductService() {
        // Seed sample data
        save(new Product(null, "MacBook Pro 16\"", new BigDecimal("2499.99"), "Electronics"));
        save(new Product(null, "Herman Miller Aeron", new BigDecimal("1395.00"), "Furniture"));
        save(new Product(null, "Sony WH-1000XM5", new BigDecimal("349.99"), "Electronics"));
    }

    /**
     * @Cacheable - Returns cached value if present, otherwise executes
     * the method and stores the result. Cache key is the product ID.
     */
    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        log.info("Cache MISS — fetching product {} from database", id);
        simulateDatabaseDelay();
        return database.get(id);
    }

    /**
     * @Cacheable on a collection - Uses a fixed key for the entire list.
     * The whole list is cached as a single entry.
     */
    @Cacheable(value = "productList", key = "'allProducts'")
    public List<Product> findAll() {
        log.info("Cache MISS — fetching all products from database");
        simulateDatabaseDelay();
        return List.copyOf(database.values());
    }

    /**
     * @CachePut - Always executes and updates the cache with the result.
     * Also evicts the product list cache since the collection changed.
     */
    @CachePut(value = "products", key = "#result.id()")
    @CacheEvict(value = "productList", key = "'allProducts'")
    public Product save(Product product) {
        Long id = product.id() != null ? product.id() : idGenerator.incrementAndGet();
        Product saved = new Product(id, product.name(), product.price(), product.category());
        database.put(id, saved);
        log.info("Saved product: {}", saved);
        return saved;
    }

    /**
     * @CachePut - Updates an existing product and refreshes its cache entry.
     */
    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "productList", key = "'allProducts'")
    public Product update(Long id, Product product) {
        Product updated = new Product(id, product.name(), product.price(), product.category());
        database.put(id, updated);
        log.info("Updated product: {}", updated);
        return updated;
    }

    /**
     * @CacheEvict - Removes the entry from cache on deletion.
     * Prevents serving stale data for deleted products.
     */
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        database.remove(id);
        log.info("Deleted product with id: {}", id);
    }

    /** Simulates a slow database query (200ms latency). */
    private void simulateDatabaseDelay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
