package com.anupam.order.service;

import com.anupam.events.OrderCreatedEvent;
import com.anupam.events.OrderItem;
import com.anupam.order.model.CreateOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Service responsible for order creation and event publishing.
 *
 * Generates a unique order ID, computes the total from line items,
 * and publishes an OrderCreatedEvent to the "orders" Kafka topic.
 * The event is keyed by orderId for consistent partitioning.
 *
 * @author Anupam
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Creates an order and publishes the corresponding event.
     *
     * @param request the order creation request with customer ID and items
     * @return the OrderCreatedEvent that was published
     */
    public OrderCreatedEvent createOrder(CreateOrderRequest request) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        // Calculate the order total from line items (price * quantity)
        BigDecimal total = request.items().stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId, request.customerId(), total, request.items(), Instant.now());

        // Publish asynchronously and log the result
        kafkaTemplate.send("orders", orderId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event for order {}: {}", orderId, ex.getMessage());
                    } else {
                        log.info("Published OrderCreatedEvent: {} to partition {} offset {}",
                                orderId, result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });

        return event;
    }
}
