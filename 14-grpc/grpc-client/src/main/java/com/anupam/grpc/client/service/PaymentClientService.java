package com.anupam.grpc.client.service;

import com.anupam.grpc.proto.ListPaymentsRequest;
import com.anupam.grpc.proto.PaymentRequest;
import com.anupam.grpc.proto.PaymentResponse;
import com.anupam.grpc.proto.PaymentServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PaymentClientService {

    private static final Logger log = LoggerFactory.getLogger(PaymentClientService.class);

    @GrpcClient("payment-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;

    public PaymentResponse getPayment(String paymentId) {
        log.info("Requesting payment: {}", paymentId);
        try {
            PaymentRequest request = PaymentRequest.newBuilder()
                    .setPaymentId(paymentId)
                    .build();
            return paymentStub.getPayment(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed: {} - {}", e.getStatus().getCode(), e.getMessage());
            throw e;
        }
    }

    public List<PaymentResponse> listPayments(String userId, int pageSize) {
        log.info("Listing payments for user: {}", userId);
        try {
            ListPaymentsRequest request = ListPaymentsRequest.newBuilder()
                    .setUserId(userId)
                    .setPageSize(pageSize)
                    .build();

            Iterator<PaymentResponse> responseIterator = paymentStub.listPayments(request);
            List<PaymentResponse> payments = new ArrayList<>();
            responseIterator.forEachRemaining(payments::add);
            return payments;
        } catch (StatusRuntimeException e) {
            log.error("gRPC stream call failed: {} - {}", e.getStatus().getCode(), e.getMessage());
            throw e;
        }
    }
}
