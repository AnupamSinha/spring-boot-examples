package com.anupam.mcp.server.tools;

import com.anupam.mcp.server.model.ExchangeRate;
import com.anupam.mcp.server.model.PaymentInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP tools exposed to remote clients via the Model Context Protocol.
 *
 * These @Tool-annotated methods are auto-registered by spring-ai-starter-mcp-server-webmvc.
 * Any MCP-compatible client (Spring AI, Claude Desktop, etc.) can discover
 * and invoke them over the SSE transport without knowing the implementation details.
 *
 * @author Anupam
 */
@Component
public class PaymentMcpTools {

    /** In-memory payment store for demonstration purposes. */
    private final Map<String, PaymentInfo> payments = new ConcurrentHashMap<>();

    /** Static exchange rates relative to USD. */
    private static final Map<String, BigDecimal> RATES_TO_USD = Map.of(
            "USD", BigDecimal.ONE,
            "EUR", new BigDecimal("0.92"),
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("149.50"),
            "INR", new BigDecimal("83.12"),
            "CAD", new BigDecimal("1.36"),
            "AUD", new BigDecimal("1.53")
    );

    public PaymentMcpTools() {
        initSampleData();
    }

    /**
     * Looks up payment details by transaction ID.
     * Throws if the transaction is not found.
     */
    @Tool(description = "Get the current status and details of a payment by its transaction ID")
    public PaymentInfo getPaymentStatus(
            @ToolParam(description = "Transaction ID, e.g. TXN-9042") String transactionId) {

        PaymentInfo info = payments.get(transactionId);
        if (info == null) {
            throw new RuntimeException("Payment not found: " + transactionId);
        }
        return info;
    }

    /**
     * Computes the exchange rate between two currencies using USD as the pivot.
     */
    @Tool(description = "Get the current exchange rate between two currencies. " +
            "Supported: USD, EUR, GBP, JPY, INR, CAD, AUD")
    public ExchangeRate getExchangeRate(
            @ToolParam(description = "Source currency code, e.g. USD") String from,
            @ToolParam(description = "Target currency code, e.g. EUR") String to) {

        BigDecimal fromToUsd = RATES_TO_USD.get(from.toUpperCase());
        BigDecimal toToUsd = RATES_TO_USD.get(to.toUpperCase());

        if (fromToUsd == null || toToUsd == null) {
            throw new IllegalArgumentException(
                    "Unsupported currency. Supported: " + RATES_TO_USD.keySet());
        }

        BigDecimal rate = toToUsd.divide(fromToUsd, 6, RoundingMode.HALF_UP);
        return new ExchangeRate(from.toUpperCase(), to.toUpperCase(), rate, LocalDateTime.now());
    }

    /**
     * Converts a payment's amount to a target currency.
     * Chains getPaymentStatus and getExchangeRate internally.
     */
    @Tool(description = "Calculate the total amount in a target currency for a given payment transaction")
    public String convertPaymentAmount(
            @ToolParam(description = "Transaction ID") String transactionId,
            @ToolParam(description = "Target currency code") String targetCurrency) {

        PaymentInfo payment = getPaymentStatus(transactionId);
        ExchangeRate rate = getExchangeRate(payment.currency(), targetCurrency);
        BigDecimal converted = payment.amount().multiply(rate.rate()).setScale(2, RoundingMode.HALF_UP);

        return String.format("%s %s = %s %s (rate: %s)",
                payment.amount(), payment.currency(),
                converted, targetCurrency, rate.rate());
    }

    /** Populates the in-memory store with sample payment data. */
    private void initSampleData() {
        payments.put("TXN-9042", new PaymentInfo(
                "TXN-9042", "COMPLETED", new BigDecimal("250.00"), "USD",
                LocalDateTime.of(2026, 8, 20, 14, 30, 0), "Alice Johnson", "Bob Smith"));
        payments.put("TXN-9043", new PaymentInfo(
                "TXN-9043", "PENDING", new BigDecimal("1200.50"), "USD",
                LocalDateTime.of(2026, 8, 21, 9, 15, 0), "Charlie Brown", "Diana Prince"));
        payments.put("TXN-9044", new PaymentInfo(
                "TXN-9044", "FAILED", new BigDecimal("75.00"), "EUR",
                LocalDateTime.of(2026, 8, 21, 16, 45, 0), "Eve Wilson", "Frank Castle"));
    }
}
