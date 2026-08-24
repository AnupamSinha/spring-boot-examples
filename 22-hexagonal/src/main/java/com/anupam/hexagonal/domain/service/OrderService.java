package com.anupam.hexagonal.domain.service;

import com.anupam.hexagonal.domain.model.Order;
import com.anupam.hexagonal.domain.port.in.CreateOrderUseCase;
import com.anupam.hexagonal.domain.port.in.GetOrderUseCase;
import com.anupam.hexagonal.domain.port.out.OrderRepository;
import com.anupam.hexagonal.domain.port.out.PaymentGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Domain service — implements use cases using pure domain logic.
 * <p>
 * Orchestrates domain entities and output ports to fulfill the application's
 * use cases. This service contains no infrastructure concerns; it relies on
 * ports for persistence and payment processing.
 * </p>
 *
 * @author Anupam
 */
@Service
@Transactional
public class OrderService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    /**
     * Constructs the order service with required output port dependencies.
     *
     * @param orderRepository the persistence port for order storage
     * @param paymentGateway  the payment port for processing charges
     */
    public OrderService(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    /**
     * Creates a new order: maps command to domain model, processes payment,
     * and persists the confirmed order.
     *
     * @param command the order creation command containing customer and items
     * @return the persisted domain order with final status
     */
    @Override
    public Order createOrder(CreateOrderCommand command) {
        // Map command items to domain line items
        var lineItems = command.items().stream()
            .map(item -> new Order.LineItem(
                item.productId(),
                item.productName(),
                item.quantity(),
                item.unitPrice()
            ))
            .toList();

        // Create domain entity (validates invariants like non-empty items)
        Order order = Order.create(command.customerId(), lineItems);

        // Process payment via output port
        order.startPayment();
        var paymentResult = paymentGateway.charge(
            order.getCustomerId(),
            order.totalAmount(),
            "USD"
        );

        // Confirm order only if payment succeeded
        if (paymentResult.status() == PaymentGateway.PaymentStatus.SUCCESS) {
            order.confirm();
        }

        // Persist via output port and return the saved order
        return orderRepository.save(order);
    }

    /**
     * Retrieves a single order by its identifier (read-only transaction).
     *
     * @param orderId the order identifier
     * @return an Optional containing the order if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Retrieves all orders for a specific customer (read-only transaction).
     *
     * @param customerId the customer identifier
     * @return list of orders belonging to the customer
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
