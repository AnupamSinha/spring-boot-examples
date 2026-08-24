package com.anupam.grpc.server.service;

import com.anupam.grpc.proto.ListPaymentsRequest;
import com.anupam.grpc.proto.PaymentRequest;
import com.anupam.grpc.proto.PaymentResponse;
import com.anupam.grpc.proto.PaymentServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * gRPC service implementation for payment operations.
 *
 * Implements the PaymentService proto definition with:
 * - Unary RPC: getPayment (single request/response)
 * - Server streaming RPC: listPayments (single request, stream of responses)
 *
 * Uses proper gRPC Status codes for error handling (NOT_FOUND, INVALID_ARGUMENT).
 *
 * @author Anupam
 */
@GrpcService
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

    /** In-memory payment store seeded with sample data. */
    private final Map<String, PaymentResponse> payments = new ConcurrentHashMap<>();

    public PaymentGrpcService() {
        // Seed sample data for demonstration
        payments.put("PAY-001", PaymentResponse.newBuilder()
                .setPaymentId("PAY-001")
                .setUserId("USER-1")
                .setAmount(99.99)
                .setCurrency("USD")
                .setStatus("COMPLETED")
                .setCreatedAt(Instant.now().toString())
                .build());
        payments.put("PAY-002", PaymentResponse.newBuilder()
                .setPaymentId("PAY-002")
                .setUserId("USER-1")
                .setAmount(49.50)
                .setCurrency("EUR")
                .setStatus("PENDING")
                .setCreatedAt(Instant.now().toString())
                .build());
        payments.put("PAY-003", PaymentResponse.newBuilder()
                .setPaymentId("PAY-003")
                .setUserId("USER-2")
                .setAmount(200.00)
                .setCurrency("USD")
                .setStatus("COMPLETED")
                .setCreatedAt(Instant.now().toString())
                .build());
    }

    /**
     * Unary RPC: retrieves a single payment by ID.
     * Returns INVALID_ARGUMENT if ID is empty, NOT_FOUND if payment doesn't exist.
     */
    @Override
    public void getPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        String paymentId = request.getPaymentId();

        if (paymentId == null || paymentId.isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Payment ID must not be empty")
                    .asRuntimeException());
            return;
        }

        PaymentResponse payment = payments.get(paymentId);

        if (payment == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Payment not found: " + paymentId)
                    .asRuntimeException());
            return;
        }

        responseObserver.onNext(payment);
        responseObserver.onCompleted();
    }

    /**
     * Server streaming RPC: streams all payments for a given user.
     * Each matching payment is sent as a separate message in the stream.
     */
    @Override
    public void listPayments(ListPaymentsRequest request, StreamObserver<PaymentResponse> responseObserver) {
        String userId = request.getUserId();
        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;

        if (userId == null || userId.isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("User ID must not be empty")
                    .asRuntimeException());
            return;
        }

        payments.values().stream()
                .filter(p -> p.getUserId().equals(userId))
                .limit(pageSize)
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }
}
