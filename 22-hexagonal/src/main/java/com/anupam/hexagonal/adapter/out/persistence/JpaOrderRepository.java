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
 * Output adapter — implements the domain's OrderRepository port using JPA.
 * Handles mapping between domain model and JPA entity.
 */
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springRepo;
    private final ObjectMapper objectMapper;

    public JpaOrderRepository(SpringDataOrderRepository springRepo, ObjectMapper objectMapper) {
        this.springRepo = springRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        springRepo.save(entity);
        return order;
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return springRepo.findById(orderId).map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return springRepo.findByCustomerId(customerId).stream()
            .map(this::toDomain)
            .toList();
    }

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

interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByCustomerId(String customerId);
}
