package com.anupam.basics.service;

import com.anupam.basics.exception.ResourceNotFoundException;
import com.anupam.basics.model.Product;
import com.anupam.basics.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for product business logic.
 *
 * Handles CRUD operations and throws {@link ResourceNotFoundException}
 * when a requested product does not exist.
 * All public methods run within a transaction.
 *
 * @author Anupam
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** Retrieves all products from the database. */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product ID
     * @return the product entity
     * @throws ResourceNotFoundException if no product exists with the given ID
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /** Retrieves all products matching the given category. */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /** Persists a new product and returns the saved entity with generated ID. */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product's name, price, and category.
     *
     * @param id             the ID of the product to update
     * @param productDetails the new field values
     * @return the updated product entity
     * @throws ResourceNotFoundException if no product exists with the given ID
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        return productRepository.save(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws ResourceNotFoundException if no product exists with the given ID
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
