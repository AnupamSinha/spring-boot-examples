package com.anupam.graphql.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a product review.
 * <p>
 * Mapped to the "reviews" table. Each review belongs to exactly one {@link Product}
 * via a many-to-one relationship (lazy-loaded for performance).
 * </p>
 *
 * @author Anupam
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** The product this review belongs to (lazy-loaded to avoid unnecessary joins). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Default constructor — sets creation timestamp to now. */
    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructs a review with all required fields.
     *
     * @param author  the name of the reviewer
     * @param comment the review text
     * @param rating  the rating (typically 1-5)
     * @param product the product being reviewed
     */
    public Review(String author, String comment, int rating, Product product) {
        this();
        this.author = author;
        this.comment = comment;
        this.rating = rating;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
