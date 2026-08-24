package com.anupam.basics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity representing a product in the catalog.
 *
 * Uses Bean Validation annotations to enforce constraints at the controller layer
 * and JPA lifecycle callbacks to auto-set the creation timestamp.
 *
 * @author Anupam
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Category is required")
    private String category;

    /** Automatically set on first persist; not updated afterwards. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** JPA lifecycle callback to populate createdAt before initial insert. */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Default constructor required by JPA
    public Product() {
    }

    /**
     * Convenience constructor for creating a product with required fields.
     *
     * @param name     the product name
     * @param price    the product price (must be positive)
     * @param category the product category
     */
    public Product(String name, BigDecimal price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
