package com.anupam.outbox.controller;

import com.anupam.outbox.model.Order;
import com.anupam.outbox.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for order operations in the Outbox Pattern demo.
 * <p>
 * Provides endpoints to create, retrieve, and confirm orders. Each state-changing
 * operation also writes an outbox event within the same database transaction.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructs the controller with the required order service.
     *
     * @param orderService the service handling order operations and outbox events
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order and writes an "OrderCreated" event to the outbox table.
     *
     * @param order the order data from the request body
     * @return the saved order with generated ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    /**
     * Retrieves all orders.
     *
     * @return list of all orders
     */
    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the order ID
     * @return the order if found, or HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Confirms an order and writes an "OrderConfirmed" event to the outbox table.
     *
     * @param id the order ID to confirm
     * @return the confirmed order, or HTTP 404 if not found
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.confirmOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
