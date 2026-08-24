package com.anupam.mongo.service;

import com.anupam.mongo.model.Product;
import com.anupam.mongo.repository.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service layer encapsulating product business logic and MongoDB operations.
 * <p>
 * Provides basic CRUD operations via the repository and advanced analytics
 * using MongoDB's aggregation framework through {@link MongoTemplate}.
 * </p>
 *
 * @author Anupam
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * Constructs the product service with required dependencies.
     *
     * @param productRepository the Spring Data MongoDB repository
     * @param mongoTemplate     the template for advanced MongoDB operations (aggregations)
     */
    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Retrieves all products from the collection.
     *
     * @return list of all products
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Finds a product by its unique document ID.
     *
     * @param id the product document ID
     * @return an Optional containing the product if found
     */
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Saves a new product, setting creation and update timestamps.
     *
     * @param product the product to save
     * @return the saved product with generated ID
     */
    public Product save(Product product) {
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }

    /**
     * Updates an existing product by assigning the given ID and refreshing the update timestamp.
     *
     * @param id      the document ID of the product to update
     * @param product the updated product data
     * @return the saved (updated) product
     */
    public Product update(String id, Product product) {
        product.setId(id);
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }

    /**
     * Deletes a product by its document ID.
     *
     * @param id the product document ID
     */
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    /**
     * Finds all products in the specified category.
     *
     * @param category the category to filter by
     * @return list of products in the category
     */
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Searches products by name using a case-insensitive regex.
     *
     * @param name the name pattern to search for
     * @return list of matching products
     */
    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    /**
     * Finds products within a price range.
     *
     * @param min the minimum price
     * @param max the maximum price
     * @return list of products in the price range
     */
    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceRange(min, max);
    }

    /**
     * Aggregation pipeline: groups products by category and computes
     * total price, product count, and average price per category.
     *
     * @return list of aggregation result documents containing category summaries
     */
    public List<Map> aggregateByCategorySummary() {
        // Stage 1: Group by category and compute aggregate metrics
        GroupOperation groupByCategory = Aggregation.group("category")
                .sum("price").as("totalPrice")
                .count().as("productCount")
                .avg("price").as("averagePrice");

        // Stage 2: Project fields into a cleaner shape (rename _id to category)
        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("category")
                .andInclude("totalPrice", "productCount", "averagePrice")
                .andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(groupByCategory, project);

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "products", Map.class);

        return results.getMappedResults();
    }

    /**
     * Aggregation pipeline: filters products above the given minimum price,
     * then groups by category with count and total value.
     *
     * @param minPrice the minimum price threshold for filtering
     * @return list of aggregation result documents for expensive products by category
     */
    public List<Map> aggregateExpensiveByCategory(BigDecimal minPrice) {
        // Stage 1: Match only products with price >= minPrice
        MatchOperation match = Aggregation.match(
                Criteria.where("price").gte(minPrice));

        // Stage 2: Group by category and compute count + total value
        GroupOperation group = Aggregation.group("category")
                .count().as("count")
                .sum("price").as("totalValue");

        // Stage 3: Project into a readable format
        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("category")
                .andInclude("count", "totalValue")
                .andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(match, group, project);

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "products", Map.class);

        return results.getMappedResults();
    }
}
