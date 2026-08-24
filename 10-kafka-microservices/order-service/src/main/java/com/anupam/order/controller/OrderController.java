package com.anupam.order.controller;

import com.anupam.events.OrderCreatedEvent;
import com.anupam.order.model.CreateOrderRequest;
import com.anupam.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderCreatedEvent> createOrder(@RequestBody CreateOrderRequest request) {
        OrderCreatedEvent event = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}
