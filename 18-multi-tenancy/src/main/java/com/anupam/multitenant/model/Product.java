package com.anupam.multitenant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity representing a product in the multi-tenant system.
 * <p>
 * Each tenant has its own isolated product table in a separate database schema.
 * The entity is automatically mapped to the correct schema by the tenant routing
 * data source.
 * </p>
 *
 * @author Anupam
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Default no-arg constructor required by JPA. */
    public Product() {
    }

    /**
     * Creates a new product with the given name and price.
     * Automatically sets the creation timestamp to the current time.
     *
     * @param name  the product name
     * @param price the product price
     */
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Returns the product's unique identifier.
     *
     * @return the product ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the product's unique identifier.
     *
     * @param id the product ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name the product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product price.
     *
     * @return the product price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     *
     * @param price the product price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Returns the timestamp when the product was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
