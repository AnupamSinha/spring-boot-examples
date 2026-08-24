package com.anupam.cqrs.command.handler;

import com.anupam.cqrs.command.CreateOrderCommand;
import com.anupam.cqrs.event.OrderCreatedEvent;
import com.anupam.cqrs.event.store.EventStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderCommandHandler {

    private final EventStore eventStore;
    private final ApplicationEventPublisher eventPublisher;

    public OrderCommandHandler(EventStore eventStore, ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    public String handle(CreateOrderCommand command) {
        // Generate aggregate ID
        String orderId = UUID.randomUUID().toString();

        // Calculate total
        BigDecimal total = command.items().stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Build event
        var items = command.items().stream()
            .map(i -> new OrderCreatedEvent.EventItem(i.productId(), i.quantity(), i.price()))
            .toList();

        var event = new OrderCreatedEvent(
            UUID.randomUUID().toString(),
            orderId,
            command.customerId(),
            items,
            total,
            Instant.now()
        );

        // Persist event to event store (source of truth)
        eventStore.append(orderId, event);

        // Publish event for projections to consume
        eventPublisher.publishEvent(event);

        return orderId;
    }
}
