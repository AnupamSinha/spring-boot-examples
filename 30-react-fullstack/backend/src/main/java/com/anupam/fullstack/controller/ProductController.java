package com.anupam.fullstack.controller;

import com.anupam.fullstack.model.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller providing CRUD operations for products.
 * <p>
 * All endpoints require JWT authentication (configured in SecurityConfig).
 * Serves as the backend API for the React frontend product management UI.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final com.anupam.fullstack.repository.ProductRepository productRepository;

    /**
     * Constructs the controller with the product repository.
     *
     * @param productRepository the JPA repository for product persistence
     */
    public ProductController(com.anupam.fullstack.repository.ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return the complete list of products
     */
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return the product entity
     * @throws EntityNotFoundException if no product exists with the given ID
     */
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    /**
     * Creates a new product and persists it to the database.
     *
     * @param product the validated product data from the request body
     * @return the saved product with its generated ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product with new data.
     *
     * @param id      the ID of the product to update
     * @param product the validated product data with updated fields
     * @return the updated product entity
     * @throws EntityNotFoundException if no product exists with the given ID
     */
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
        // Update mutable fields
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());
        return productRepository.save(existing);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
