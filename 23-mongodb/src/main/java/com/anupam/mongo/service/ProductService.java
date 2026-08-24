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

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }

    public Product update(String id, Product product) {
        product.setId(id);
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceRange(min, max);
    }

    /**
     * Aggregation pipeline: group by category, compute total price and product count.
     */
    public List<Map> aggregateByCategorySummary() {
        GroupOperation groupByCategory = Aggregation.group("category")
                .sum("price").as("totalPrice")
                .count().as("productCount")
                .avg("price").as("averagePrice");

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
     * Aggregation pipeline: filter by minimum price, then group by category.
     */
    public List<Map> aggregateExpensiveByCategory(BigDecimal minPrice) {
        MatchOperation match = Aggregation.match(
                Criteria.where("price").gte(minPrice));

        GroupOperation group = Aggregation.group("category")
                .count().as("count")
                .sum("price").as("totalValue");

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
