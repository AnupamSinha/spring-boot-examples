package com.anupam.testcontainers.model;

import java.math.BigDecimal;

public record PaymentEvent(String transactionId, String status, BigDecimal amount) {}
