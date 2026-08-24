package com.anupam.outbox.service;

import com.anupam.outbox.model.Order;
import com.anupam.outbox.model.OutboxEvent;
import com.anupam.outbox.repository.OrderRepository;
import com.anupam.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for order operations implementing the Transactional Outbox Pattern.
 * <p>
 * Every state-changing operation saves both the order entity and an outbox event
 * within the same database transaction. This eliminates the dual-write problem:
 * if the transaction commits, the event is guaranteed to be in the outbox and
 * will eventually be published by the relay.
 * </p>
 *
 * @author Anupam
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructs the order service with required dependencies.
     *
     * @param orderRepository  the JPA repository for order persistence
     * @param outboxRepository the JPA repository for outbox event persistence
     * @param objectMapper     the Jackson mapper for serializing events to JSON
     */
    public OrderService(OrderRepository orderRepository,
                        OutboxRepository outboxRepository,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates an order AND saves an outbox event in the SAME transaction.
     * <p>
     * This guarantees that if the order is saved, the event is also saved —
     * eliminating the dual-write problem. The outbox relay will later publish
     * the event to Kafka.
     * </p>
     *
     * @param order the order to create
     * @return the saved order with generated ID
     */
    @Transactional
    public Order createOrder(Order order) {
        // Persist the order
        Order savedOrder = orderRepository.save(order);

        // Write the "OrderCreated" event to the outbox in the same transaction
        OutboxEvent event = new OutboxEvent(
                "Order",
                savedOrder.getId().toString(),
                "OrderCreated",
                toJson(savedOrder)
        );
        outboxRepository.save(event);

        return savedOrder;
    }

    /**
     * Confirms an existing order and writes an "OrderConfirmed" event to the outbox.
     * <p>
     * Both the status update and the outbox event are saved atomically in one transaction.
     * </p>
     *
     * @param orderId the ID of the order to confirm
     * @return the confirmed order
     * @throws RuntimeException if the order is not found
     */
    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Update the order status
        order.setStatus(Order.OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        // Write the "OrderConfirmed" event to the outbox in the same transaction
        OutboxEvent event = new OutboxEvent(
                "Order",
                savedOrder.getId().toString(),
                "OrderConfirmed",
                toJson(savedOrder)
        );
        outboxRepository.save(event);

        return savedOrder;
    }

    /**
     * Retrieves all orders.
     *
     * @return list of all orders
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * Finds an order by its unique identifier.
     *
     * @param id the order ID
     * @return an Optional containing the order if found
     */
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Serializes an object to its JSON representation.
     *
     * @param obj the object to serialize
     * @return the JSON string
     * @throws RuntimeException if serialization fails
     */
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to JSON", e);
        }
    }
}
