package com.anupam.htmx.service;

import com.anupam.htmx.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public ProductService() {
        create("Mechanical Keyboard", new BigDecimal("129.99"));
        create("USB-C Hub", new BigDecimal("49.99"));
        create("Monitor Stand", new BigDecimal("79.99"));
        create("Webcam HD", new BigDecimal("89.99"));
        create("Noise-Cancelling Headphones", new BigDecimal("249.99"));
    }

    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.id().equals(id))
                .findFirst();
    }

    public Product create(String name, BigDecimal price) {
        Product product = new Product(idGenerator.incrementAndGet(), name, price);
        products.add(product);
        return product;
    }

    public boolean delete(Long id) {
        return products.removeIf(p -> p.id().equals(id));
    }

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
