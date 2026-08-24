package com.anupam.cqrs.query;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderQueryService {

    private final OrderReadModelRepository repository;

    public OrderQueryService(OrderReadModelRepository repository) {
        this.repository = repository;
    }

    public List<OrderReadModel> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    public Optional<OrderReadModel> findById(String orderId) {
        return repository.findById(orderId);
    }

    public List<OrderReadModel> findAll() {
        return repository.findAll();
    }
}

@Repository
interface OrderReadModelRepository extends JpaRepository<OrderReadModel, String> {
    List<OrderReadModel> findByCustomerId(String customerId);
}

@Entity
@Table(name = "orders_read_model")
class OrderReadModel {

    @Id
    private String orderId;
    private String customerId;
    private int itemCount;
    private BigDecimal totalAmount;
    private String status;
    private Instant createdAt;

    protected OrderReadModel() {}

    public OrderReadModel(String orderId, String customerId, int itemCount,
                          BigDecimal totalAmount, String status, Instant createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.itemCount = itemCount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public int getItemCount() { return itemCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
