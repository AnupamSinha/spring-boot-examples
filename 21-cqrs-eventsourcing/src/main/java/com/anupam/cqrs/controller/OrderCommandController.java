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

/**
 * REST controller responsible for the command (write) side of the CQRS architecture.
 * <p>
 * Accepts order creation requests, delegates to the command handler, and returns
 * an ACCEPTED response with a location header pointing to the read-side endpoint.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/commands/orders")
public class OrderCommandController {

    private final OrderCommandHandler commandHandler;

    /**
     * Constructs the controller with the required command handler dependency.
     *
     * @param commandHandler the handler responsible for processing order commands
     */
    public OrderCommandController(OrderCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Accepts and processes a new order creation command.
     * <p>
     * Returns HTTP 202 (Accepted) to indicate the command has been received and
     * will be processed asynchronously. The read model will be eventually consistent.
     * </p>
     *
     * @param command the validated order creation command from the request body
     * @return a response containing the order ID, status, and a location for querying
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        String orderId = commandHandler.handle(command);
        // Return ACCEPTED with a location header pointing to the query-side endpoint
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
