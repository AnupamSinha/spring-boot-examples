package com.anupam.cqrs.controller;

import com.anupam.cqrs.command.CreateOrderCommand;
import com.anupam.cqrs.command.handler.OrderCommandHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/commands/orders")
public class OrderCommandController {

    private final OrderCommandHandler commandHandler;

    public OrderCommandController(OrderCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        String orderId = commandHandler.handle(command);
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .location(URI.create("/api/queries/orders/" + orderId))
            .body(Map.of(
                "orderId", orderId,
                "status", "ACCEPTED",
                "message", "Order command accepted. Read model will be eventually consistent."
            ));
    }
}
