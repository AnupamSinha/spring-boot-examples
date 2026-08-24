package com.anupam.cqrs.command.handler;

import com.anupam.cqrs.command.CreateOrderCommand;
import com.anupam.cqrs.event.OrderCreatedEvent;
import com.anupam.cqrs.event.store.EventStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Handles order-related commands by producing domain events and persisting them
 * to the event store. This is the write-side handler in the CQRS architecture.
 * <p>
 * The handler orchestrates the following flow:
 * <ol>
 *   <li>Generates a unique aggregate (order) identifier</li>
 *   <li>Computes derived data (e.g., total amount)</li>
 *   <li>Builds and persists the domain event to the event store</li>
 *   <li>Publishes the event so projections can update the read model</li>
 * </ol>
 * </p>
 *
 * @author Anupam
 */
@Service
public class OrderCommandHandler {

    private final EventStore eventStore;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Constructs an {@code OrderCommandHandler} with required dependencies.
     *
     * @param eventStore     the event store used to persist domain events
     * @param eventPublisher the Spring event publisher for broadcasting events to listeners
     */
    public OrderCommandHandler(EventStore eventStore, ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles a {@link CreateOrderCommand} by generating an order event and
     * persisting it to the event store.
     *
     * @param command the create order command containing customer and item details
     * @return the generated order ID (aggregate identifier)
     */
    public String handle(CreateOrderCommand command) {
        // Generate a unique aggregate ID for the new order
        String orderId = UUID.randomUUID().toString();

        // Calculate the total order amount by summing (price * quantity) for each item
        BigDecimal total = command.items().stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Map command items to event items
        var items = command.items().stream()
            .map(i -> new OrderCreatedEvent.EventItem(i.productId(), i.quantity(), i.price()))
            .toList();

        // Build the domain event capturing the full state change
        var event = new OrderCreatedEvent(
            UUID.randomUUID().toString(),
            orderId,
            command.customerId(),
            items,
            total,
            Instant.now()
        );

        // Persist event to the event store (single source of truth)
        eventStore.append(orderId, event);

        // Publish event so projections can rebuild the read model
        eventPublisher.publishEvent(event);

        return orderId;
    }
}
