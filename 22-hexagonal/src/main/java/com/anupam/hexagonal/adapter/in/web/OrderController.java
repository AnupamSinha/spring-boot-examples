package com.anupam.hexagonal.adapter.in.web;

import com.anupam.hexagonal.domain.model.Order;
import com.anupam.hexagonal.domain.port.in.CreateOrderUseCase;
import com.anupam.hexagonal.domain.port.in.CreateOrderUseCase.CreateOrderCommand;
import com.anupam.hexagonal.domain.port.in.GetOrderUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Input adapter — translates HTTP requests into use case calls.
 * <p>
 * Depends on input ports (interfaces), not on the domain service directly.
 * This ensures the web layer is decoupled from the business logic implementation.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    /**
     * Constructs the controller with the required use case ports.
     *
     * @param createOrderUseCase the port for creating orders
     * @param getOrderUseCase    the port for retrieving orders
     */
    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    /**
     * Creates a new order by translating the HTTP request into a use case command.
     *
     * @param request the validated order creation request payload
     * @return HTTP 201 with the created order response
     */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        // Map the HTTP request DTO to a use case command object
        var command = new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(i -> new CreateOrderCommand.OrderItemCommand(
                    i.productId(), i.productName(), i.quantity(), i.unitPrice()))
                .toList()
        );

        Order order = createOrderUseCase.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param orderId the unique identifier of the order
     * @return the order response or HTTP 404 if not found
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(orderId)
            .map(order -> ResponseEntity.ok(toResponse(order)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all orders belonging to a specific customer.
     *
     * @param customerId the unique identifier of the customer
     * @return list of order responses for the customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getByCustomer(@PathVariable String customerId) {
        var orders = getOrderUseCase.getOrdersByCustomer(customerId)
            .stream()
            .map(this::toResponse)
            .toList();
        return ResponseEntity.ok(orders);
    }

    /**
     * Maps a domain Order object to an HTTP response DTO.
     *
     * @param order the domain order entity
     * @return the response DTO suitable for serialization
     */
    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getStatus().name(),
            order.totalAmount(),
            order.getItems().size()
        );
    }

    // Request / Response DTOs

    /** Request payload for creating a new order. */
    record CreateOrderRequest(
        @NotBlank String customerId,
        @NotEmpty List<OrderItemRequest> items
    ) {}

    /** Individual item within an order creation request. */
    record OrderItemRequest(String productId, String productName, int quantity, BigDecimal unitPrice) {}

    /** Response DTO representing an order summary. */
    record OrderResponse(String orderId, String customerId, String status, BigDecimal total, int itemCount) {}
}
