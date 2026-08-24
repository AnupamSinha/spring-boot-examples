package com.anupam.fullstack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * JPA entity representing a product in the catalog.
 * <p>
 * Includes Bean Validation constraints to enforce data integrity
 * at the API boundary before persistence.
 * </p>
 *
 * @author Anupam
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String description;

    /**
     * Default no-arg constructor required by JPA.
     */
    public Product() {}

    /**
     * Constructs a product with the given attributes.
     *
     * @param name        the product name
     * @param price       the product price (must be positive)
     * @param description an optional product description
     */
    public Product(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Returns the product ID.
     *
     * @return the auto-generated ID
     */
    public Long getId() { return id; }

    /**
     * Sets the product ID.
     *
     * @param id the product ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Returns the product name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets the product name.
     *
     * @param name the product name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the product price.
     *
     * @return the price
     */
    public BigDecimal getPrice() { return price; }

    /**
     * Sets the product price.
     *
     * @param price the product price (must be positive)
     */
    public void setPrice(BigDecimal price) { this.price = price; }

    /**
     * Returns the product description.
     *
     * @return the description, or null if not set
     */
    public String getDescription() { return description; }

    /**
     * Sets the product description.
     *
     * @param description the product description
     */
    public void setDescription(String description) { this.description = description; }
}
