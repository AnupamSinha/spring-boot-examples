package com.anupam.cqrs.controller;

import com.anupam.cqrs.query.OrderQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for the query (read) side of the CQRS architecture.
 * <p>
 * Provides endpoints to retrieve order data from the read-optimized model.
 * This controller never modifies state — it only queries the projection.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/queries/orders")
public class OrderQueryController {

    private final OrderQueryService queryService;

    /**
     * Constructs the controller with the required query service dependency.
     *
     * @param queryService the service responsible for querying the read model
     */
    public OrderQueryController(OrderQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Retrieves all orders for a given customer.
     *
     * @param customerId the unique identifier of the customer
     * @return a list of orders belonging to the specified customer
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getOrdersByCustomer(@PathVariable String customerId) {
        var orders = queryService.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves a single order by its unique identifier.
     *
     * @param orderId the unique identifier of the order
     * @return the order if found, or HTTP 404 if not
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        return queryService.findById(orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all orders from the read model.
     *
     * @return a list of all orders
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(queryService.findAll());
    }
}
