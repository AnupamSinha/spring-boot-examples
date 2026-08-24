package com.anupam.htmx.service;

import com.anupam.htmx.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory product service providing CRUD operations and search.
 * <p>
 * Uses a thread-safe {@link CopyOnWriteArrayList} for storage and an
 * {@link AtomicLong} for ID generation. Pre-populated with sample data
 * on construction for demo purposes.
 * </p>
 *
 * @author Anupam
 */
@Service
public class ProductService {

    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * Initializes the service with sample product data for demonstration.
     */
    public ProductService() {
        create("Mechanical Keyboard", new BigDecimal("129.99"));
        create("USB-C Hub", new BigDecimal("49.99"));
        create("Monitor Stand", new BigDecimal("79.99"));
        create("Webcam HD", new BigDecimal("89.99"));
        create("Noise-Cancelling Headphones", new BigDecimal("249.99"));
    }

    /**
     * Returns an unmodifiable list of all products.
     *
     * @return all products in the catalog
     */
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Finds a product by its unique ID.
     *
     * @param id the product ID to look up
     * @return an Optional containing the product if found
     */
    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.id().equals(id))
                .findFirst();
    }

    /**
     * Creates a new product with an auto-generated ID.
     *
     * @param name  the product name
     * @param price the product price
     * @return the newly created product with its assigned ID
     */
    public Product create(String name, BigDecimal price) {
        Product product = new Product(idGenerator.incrementAndGet(), name, price);
        products.add(product);
        return product;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID to remove
     * @return true if the product was found and removed, false otherwise
     */
    public boolean delete(Long id) {
        return products.removeIf(p -> p.id().equals(id));
    }

    /**
     * Searches products by name using case-insensitive substring matching.
     * <p>
     * Returns all products if the query is null or blank.
     * </p>
     *
     * @param query the search query string
     * @return a list of products matching the search criteria
     */
    public List<Product> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        String lowerQuery = query.toLowerCase();
        return products.stream()
                .filter(p -> p.name().toLowerCase().contains(lowerQuery))
                .toList();
    }
}
