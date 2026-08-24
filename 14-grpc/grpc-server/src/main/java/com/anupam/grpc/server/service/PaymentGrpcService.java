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

@GrpcService
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final Map<String, PaymentResponse> payments = new ConcurrentHashMap<>();

    public PaymentGrpcService() {
        // Seed some sample data
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
