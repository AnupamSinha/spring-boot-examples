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

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        OutboxRepository outboxRepository,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates an order AND saves an outbox event in the SAME transaction.
     * This guarantees that if the order is saved, the event is also saved —
     * eliminating the dual-write problem.
     */
    @Transactional
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);

        OutboxEvent event = new OutboxEvent(
                "Order",
                savedOrder.getId().toString(),
                "OrderCreated",
                toJson(savedOrder)
        );
        outboxRepository.save(event);

        return savedOrder;
    }

    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(Order.OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        OutboxEvent event = new OutboxEvent(
                "Order",
                savedOrder.getId().toString(),
                "OrderConfirmed",
                toJson(savedOrder)
        );
        outboxRepository.save(event);

        return savedOrder;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to JSON", e);
        }
    }
}
