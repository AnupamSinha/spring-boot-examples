package com.anupam.mongo.controller;

import com.anupam.mongo.model.Product;
import com.anupam.mongo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST controller providing CRUD and query operations for products stored in MongoDB.
 * <p>
 * Exposes endpoints for basic CRUD, category-based filtering, text search,
 * and MongoDB aggregation pipeline results.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructs the controller with the required product service.
     *
     * @param productService the service encapsulating product business logic
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products from the MongoDB collection.
     *
     * @return list of all products
     */
    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the product document ID
     * @return the product if found, or HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable String id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new product document in the MongoDB collection.
     *
     * @param product the product data from the request body
     * @return the created product with a generated ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param id      the product document ID
     * @param product the updated product data
     * @return the updated product, or HTTP 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
        return productService.findById(id)
                .map(existing -> ResponseEntity.ok(productService.update(id, product)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product document ID to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.deleteById(id);
    }

    /**
     * Finds all products in the specified category.
     *
     * @param category the category to filter by
     * @return list of matching products
     */
    @GetMapping("/category/{category}")
    public List<Product> findByCategory(@PathVariable String category) {
        return productService.findByCategory(category);
    }

    /**
     * Searches products by name using a case-insensitive regex match.
     *
     * @param name the search term
     * @return list of products matching the name pattern
     */
    @GetMapping("/search")
    public List<Product> searchByName(@RequestParam String name) {
        return productService.searchByName(name);
    }

    /**
     * Executes an aggregation pipeline that groups products by category
     * and computes summary statistics (total price, count, average).
     *
     * @return list of aggregation result documents
     */
    @GetMapping("/aggregation")
    public List<Map> aggregateByCategorySummary() {
        return productService.aggregateByCategorySummary();
    }

    /**
     * Executes an aggregation pipeline that filters products above the minimum price
     * and groups results by category.
     *
     * @param minPrice the minimum price threshold (defaults to 100)
     * @return list of aggregation result documents
     */
    @GetMapping("/aggregation/expensive")
    public List<Map> aggregateExpensive(@RequestParam(defaultValue = "100") BigDecimal minPrice) {
        return productService.aggregateExpensiveByCategory(minPrice);
    }
}
