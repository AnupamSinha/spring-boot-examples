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

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    public ProductService(ProductRepository productRepository, MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.meterRegistry = meterRegistry;
    }

    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll"));
        return products;
    }

    @Cacheable(value = "productById", key = "#id")
    public Optional<Product> getProductById(Long id) {
        meterRegistry.counter("product.service.getById.calls").increment();
        return productRepository.findById(id);
    }

    public List<Product> getProductsUncached() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll.uncached"));
        return products;
    }

    @Cacheable(value = "products", key = "#category")
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Product>> getAllProductsAsync() {
        Timer.Sample sample = Timer.start(meterRegistry);
        List<Product> products = productRepository.findAll();
        sample.stop(meterRegistry.timer("product.service.getAll.async"));
        return CompletableFuture.completedFuture(products);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Product>> searchProductsAsync(String name) {
        return CompletableFuture.completedFuture(
                productRepository.findByNameContainingIgnoreCase(name)
        );
    }

    @Transactional
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public Product createProduct(Product product) {
        meterRegistry.counter("product.service.create.calls").increment();
        return productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = {"products", "productById"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
