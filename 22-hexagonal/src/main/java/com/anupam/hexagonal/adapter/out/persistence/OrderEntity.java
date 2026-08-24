package com.anupam.hexagonal.adapter.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * JPA entity — lives in the adapter layer, not the domain.
 * Maps between database schema and domain model.
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private int itemCount;

    @Column(nullable = false)
    private String itemsJson; // Simplified: store items as JSON string

    @Column(nullable = false)
    private Instant createdAt;

    protected OrderEntity() {}

    public OrderEntity(String id, String customerId, String status,
                       BigDecimal totalAmount, int itemCount, String itemsJson, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.itemCount = itemCount;
        this.itemsJson = itemsJson;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getItemCount() { return itemCount; }
    public String getItemsJson() { return itemsJson; }
    public Instant getCreatedAt() { return createdAt; }

    public void setStatus(String status) { this.status = status; }
}
