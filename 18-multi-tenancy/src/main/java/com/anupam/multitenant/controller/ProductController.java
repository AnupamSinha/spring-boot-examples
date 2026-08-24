package com.anupam.multitenant.controller;

import com.anupam.multitenant.model.Product;
import com.anupam.multitenant.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CRUD REST controller for managing products in a multi-tenant context.
 * <p>
 * The tenant is resolved automatically by {@link com.anupam.multitenant.config.TenantFilter}
 * from the X-Tenant-ID header — no explicit tenant logic is needed in this controller.
 * All operations are automatically scoped to the current tenant's data.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * Constructs the controller with the required product repository.
     *
     * @param productRepository the JPA repository for product persistence
     */
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products for the current tenant.
     *
     * @return a list of all products in the tenant's schema
     */
    @GetMapping
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the product identifier
     * @return the product if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new product in the current tenant's schema.
     *
     * @param product the product data to persist
     * @return the created product with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Updates an existing product's name and price.
     *
     * @param id      the product identifier to update
     * @param product the updated product data
     * @return the updated product if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    return ResponseEntity.ok(productRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product identifier to delete
     * @return 204 No Content if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
