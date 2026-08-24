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
 * Orchestrates domain entities and output ports.
 */
@Service
@Transactional
public class OrderService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public OrderService(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Order createOrder(CreateOrderCommand command) {
        // Map command to domain model
        var lineItems = command.items().stream()
            .map(item -> new Order.LineItem(
                item.productId(),
                item.productName(),
                item.quantity(),
                item.unitPrice()
            ))
            .toList();

        // Create domain entity (validates invariants)
        Order order = Order.create(command.customerId(), lineItems);

        // Process payment via output port
        order.startPayment();
        var paymentResult = paymentGateway.charge(
            order.getCustomerId(),
            order.totalAmount(),
            "USD"
        );

        if (paymentResult.status() == PaymentGateway.PaymentStatus.SUCCESS) {
            order.confirm();
        }

        // Persist via output port
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
