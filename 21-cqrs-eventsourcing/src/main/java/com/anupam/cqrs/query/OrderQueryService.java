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

/**
 * Service for the query (read) side of the CQRS architecture.
 * <p>
 * Provides methods to query the denormalized read model that is built
 * and maintained by projections consuming domain events.
 * </p>
 *
 * @author Anupam
 */
@Service
public class OrderQueryService {

    private final OrderReadModelRepository repository;

    /**
     * Constructs the query service with the required repository dependency.
     *
     * @param repository the JPA repository for the order read model
     */
    public OrderQueryService(OrderReadModelRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds all orders belonging to a specific customer.
     *
     * @param customerId the unique identifier of the customer
     * @return list of orders for the given customer
     */
    public List<OrderReadModel> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    /**
     * Finds a single order by its unique identifier.
     *
     * @param orderId the unique identifier of the order
     * @return an Optional containing the order if found
     */
    public Optional<OrderReadModel> findById(String orderId) {
        return repository.findById(orderId);
    }

    /**
     * Retrieves all orders from the read model.
     *
     * @return list of all orders
     */
    public List<OrderReadModel> findAll() {
        return repository.findAll();
    }
}

/**
 * Spring Data JPA repository for querying the order read model.
 *
 * @author Anupam
 */
@Repository
interface OrderReadModelRepository extends JpaRepository<OrderReadModel, String> {

    /**
     * Finds all order projections belonging to a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of order read models
     */
    List<OrderReadModel> findByCustomerId(String customerId);
}

/**
 * JPA entity representing the denormalized order read model.
 * <p>
 * This table is optimized for query performance and is populated
 * by the {@link com.anupam.cqrs.projection.OrderProjection}.
 * </p>
 *
 * @author Anupam
 */
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

    /** Default constructor required by JPA. */
    protected OrderReadModel() {}

    /**
     * Constructs a fully populated read model entry.
     *
     * @param orderId     the unique order identifier
     * @param customerId  the customer who placed the order
     * @param itemCount   the number of items in the order
     * @param totalAmount the total monetary value of the order
     * @param status      the current order status
     * @param createdAt   when the order was created
     */
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
