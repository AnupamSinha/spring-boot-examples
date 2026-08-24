package com.anupam.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * MongoDB document representing a product in the catalog.
 * <p>
 * Stored in the "products" collection. Includes indexed fields for name and category
 * to support efficient querying, and an embedded {@link Specification} sub-document
 * for product details.
 * </p>
 *
 * @author Anupam
 */
@Document(collection = "products")
public class Product {

    /** MongoDB-generated unique document identifier. */
    @Id
    private String id;

    /** Product name with an index for text-search queries. */
    @Indexed
    private String name;

    private String description;

    /** Product category with an index for filtered lookups. */
    @Indexed
    private String category;

    private BigDecimal price;

    /** Embedded sub-document containing detailed product specifications. */
    private Specification specification;

    /** Flexible tags for filtering and categorization. */
    private List<String> tags;

    private Instant createdAt;

    private Instant updatedAt;

    /**
     * Embedded record for product specifications such as brand, model, and physical attributes.
     *
     * @param brand      the manufacturer brand
     * @param model      the product model name
     * @param weight     the weight in kilograms
     * @param dimensions the physical dimensions (e.g., "30x20x10 cm")
     * @param color      the primary color
     */
    public record Specification(
            String brand,
            String model,
            double weight,
            String dimensions,
            String color
    ) {}

    /** Default constructor — sets creation and update timestamps to now. */
    public Product() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Constructs a product with all catalog attributes.
     *
     * @param name          the product name
     * @param description   the product description
     * @param category      the product category
     * @param price         the product price
     * @param specification the detailed product specifications
     * @param tags          searchable tags for the product
     */
    public Product(String name, String description, String category, BigDecimal price,
                   Specification specification, List<String> tags) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.specification = specification;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
