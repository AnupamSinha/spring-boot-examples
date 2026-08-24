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
 * Depends on input ports (interfaces), not on domain service directly.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(orderId)
            .map(order -> ResponseEntity.ok(toResponse(order)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getByCustomer(@PathVariable String customerId) {
        var orders = getOrderUseCase.getOrdersByCustomer(customerId)
            .stream()
            .map(this::toResponse)
            .toList();
        return ResponseEntity.ok(orders);
    }

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
    record CreateOrderRequest(
        @NotBlank String customerId,
        @NotEmpty List<OrderItemRequest> items
    ) {}

    record OrderItemRequest(String productId, String productName, int quantity, BigDecimal unitPrice) {}

    record OrderResponse(String orderId, String customerId, String status, BigDecimal total, int itemCount) {}
}
