package com.anupam.grpc.client.controller;

import com.anupam.grpc.client.service.PaymentClientService;
import com.anupam.grpc.proto.PaymentResponse;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentClientService paymentClientService;

    public PaymentController(PaymentClientService paymentClientService) {
        this.paymentClientService = paymentClientService;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable String paymentId) {
        try {
            PaymentResponse response = paymentClientService.getPayment(paymentId);
            return ResponseEntity.ok(toMap(response));
        } catch (StatusRuntimeException e) {
            return handleGrpcError(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listPayments(
            @RequestParam String userId,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<PaymentResponse> responses = paymentClientService.listPayments(userId, pageSize);
            List<Map<String, Object>> result = responses.stream()
                    .map(this::toMap)
                    .toList();
            return ResponseEntity.ok(result);
        } catch (StatusRuntimeException e) {
            return handleGrpcError(e);
        }
    }

    private Map<String, Object> toMap(PaymentResponse response) {
        return Map.of(
                "paymentId", response.getPaymentId(),
                "userId", response.getUserId(),
                "amount", response.getAmount(),
                "currency", response.getCurrency(),
                "status", response.getStatus(),
                "createdAt", response.getCreatedAt()
        );
    }

    private ResponseEntity<Map<String, String>> handleGrpcError(StatusRuntimeException e) {
        HttpStatus httpStatus = switch (e.getStatus().getCode()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            case DEADLINE_EXCEEDED -> HttpStatus.GATEWAY_TIMEOUT;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(httpStatus)
                .body(Map.of("error", e.getStatus().getDescription() != null
                        ? e.getStatus().getDescription()
                        : "Unknown error"));
    }
}
