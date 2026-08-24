package com.anupam.fullstack.controller;

import com.anupam.fullstack.model.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final com.anupam.fullstack.repository.ProductRepository productRepository;

    public ProductController(com.anupam.fullstack.repository.ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());
        return productRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
