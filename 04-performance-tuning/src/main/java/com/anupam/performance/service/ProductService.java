package com.anupam.performance.service;

import com.anupam.performance.model.Product;
import com.anupam.performance.repository.ProductRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Product service demonstrating caching, async execution, and metrics instrumentation.
 *
 * Key performance patterns:
 * - @Cacheable: avoids repeated database hits for the same data
 * - @CacheEvict: invalidates stale cache entries on writes
 * - @Async: offloads work to the configured thread pool
 * - Micrometer Timer/Counter: tracks latency and call counts
 *
 * @author Anupam
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    public ProductService(ProductRepository productRepository, MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Retrieves all products with caching.
     * First call hits the database; subsequent calls serve from Caffeine cache.
     */
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll"));
        return products;
    }

    /** Retrieves a product by ID with per-entity caching. */
    @Cacheable(value = "productById", key = "#id")
    public Optional<Product> getProductById(Long id) {
        meterRegistry.counter("product.service.getById.calls").increment();
        return productRepository.findById(id);
    }

    /**
     * Retrieves all products without caching.
     * Useful for comparing cached vs. uncached performance in benchmarks.
     */
    public List<Product> getProductsUncached() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll.uncached"));
        return products;
    }

    /** Retrieves products by category with caching keyed on the category name. */
    @Cacheable(value = "products", key = "#category")
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Asynchronously retrieves all products on the "taskExecutor" thread pool.
     * Returns a CompletableFuture that resolves when the query completes.
     */
    @Async("taskExecutor")
    public CompletableFuture<List<Product>> getAllProductsAsync() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll.async"));
        return CompletableFuture.completedFuture(products);
    }

    /** Asynchronous name-based search executed on the async thread pool. */
    @Async("taskExecutor")
    public CompletableFuture<List<Product>> searchProductsAsync(String name) {
        return CompletableFuture.completedFuture(
                productRepository.findByNameContainingIgnoreCase(name)
        );
    }

    /**
     * Creates a new product and evicts all product caches to ensure freshness.
     * Tracks creation count via a Micrometer counter.
     */
    @Transactional
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product createProduct(Product product) {
        meterRegistry.counter("product.service.create.calls").increment();
        return productRepository.save(product);
    }

    /** Deletes a product and evicts all caches. */
    @Transactional
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
