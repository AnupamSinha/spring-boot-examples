package com.anupam.hexagonal.adapter.out.persistence;

import com.anupam.hexagonal.domain.model.Order;
import com.anupam.hexagonal.domain.model.OrderStatus;
import com.anupam.hexagonal.domain.port.out.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Output adapter — implements the domain's {@link OrderRepository} port using JPA.
 * <p>
 * Handles mapping between the rich domain model ({@link Order}) and the persistence
 * entity ({@link OrderEntity}). Order line items are serialized to JSON for simplified storage.
 * </p>
 *
 * @author Anupam
 */
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springRepo;
    private final ObjectMapper objectMapper;

    /**
     * Constructs the repository adapter with its dependencies.
     *
     * @param springRepo   the Spring Data JPA repository for entity persistence
     * @param objectMapper the Jackson mapper for serializing/deserializing line items
     */
    public JpaOrderRepository(SpringDataOrderRepository springRepo, ObjectMapper objectMapper) {
        this.springRepo = springRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Persists the domain order by converting it to a JPA entity.
     *
     * @param order the domain order to save
     * @return the saved domain order (unchanged)
     */
    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        springRepo.save(entity);
        return order;
    }

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId the order identifier
     * @return an Optional containing the domain order if found
     */
    @Override
    public Optional<Order> findById(String orderId) {
        return springRepo.findById(orderId).map(this::toDomain);
    }

    /**
     * Retrieves all orders belonging to a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of domain orders for the customer
     */
    @Override
    public List<Order> findByCustomerId(String customerId) {
        return springRepo.findByCustomerId(customerId).stream()
            .map(this::toDomain)
            .toList();
    }

    /**
     * Converts a domain {@link Order} to a persistence {@link OrderEntity}.
     * Serializes line items to JSON for storage.
     */
    private OrderEntity toEntity(Order order) {
        String itemsJson;
        try {
            itemsJson = objectMapper.writeValueAsString(order.getItems());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order items", e);
        }

        return new OrderEntity(
            order.getId(),
            order.getCustomerId(),
            order.getStatus().name(),
            order.totalAmount(),
            order.getItems().size(),
            itemsJson,
            order.getCreatedAt()
        );
    }

    /**
     * Converts a persistence {@link OrderEntity} back to a domain {@link Order}.
     * Deserializes line items from JSON.
     */
    private Order toDomain(OrderEntity entity) {
        List<Order.LineItem> items;
        try {
            items = objectMapper.readValue(entity.getItemsJson(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize order items", e);
        }

        return new Order(
            entity.getId(),
            entity.getCustomerId(),
            items,
            OrderStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt()
        );
    }
}

/**
 * Spring Data JPA repository for {@link OrderEntity} persistence operations.
 *
 * @author Anupam
 */
interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {

    /**
     * Finds all order entities belonging to a given customer.
     *
     * @param customerId the customer identifier
     * @return list of matching order entities
     */
    List<OrderEntity> findByCustomerId(String customerId);
}
