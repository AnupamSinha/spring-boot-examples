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

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderCreatedEvent createOrder(CreateOrderRequest request) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        BigDecimal total = request.items().stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId, request.customerId(), total, request.items(), Instant.now());

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
