package com.anupam.ai.tools;

import com.anupam.ai.model.ExchangeRate;
import com.anupam.ai.model.PaymentInfo;
import com.anupam.ai.model.PaymentSummary;
import com.anupam.ai.service.ExchangeRateService;
import com.anupam.ai.service.PaymentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tools exposed to the AI model via Spring AI's @Tool annotation.
 *
 * Each method becomes a callable tool that the LLM can invoke when it determines
 * the user's question requires live data. The model sees the method name, description,
 * and parameter schema — then decides whether and when to call each tool.
 */
@Component
public class PaymentTools {

    private final PaymentService paymentService;
    private final ExchangeRateService exchangeRateService;

    public PaymentTools(PaymentService paymentService, ExchangeRateService exchangeRateService) {
        this.paymentService = paymentService;
        this.exchangeRateService = exchangeRateService;
    }

    @Tool(description = "Get the current status and details of a payment by its transaction ID. " +
            "Returns sender, receiver, amount, currency, status, and timestamp.")
    public PaymentInfo getPaymentStatus(
            @ToolParam(description = "The transaction ID, e.g. TXN-9042") String transactionId) {

        PaymentInfo info = paymentService.findByTransactionId(transactionId);
        if (info == null) {
            throw new RuntimeException("Payment not found for transaction ID: " + transactionId);
        }
        return info;
    }

    @Tool(description = "List recent payments for a customer, ordered by date descending. " +
            "Use this when the user asks about a customer's payment history.")
    public List<PaymentSummary> getRecentPayments(
            @ToolParam(description = "Customer ID (numeric)") Long customerId,
            @ToolParam(description = "Maximum number of results to return (default 5)", required = false) Integer limit) {

        int effectiveLimit = (limit != null) ? limit : 5;
        List<PaymentSummary> results = paymentService.getRecentPayments(customerId, effectiveLimit);

        if (results.isEmpty()) {
            throw new RuntimeException("No payments found for customer ID: " + customerId);
        }
        return results;
    }

    @Tool(description = "Get the current exchange rate between two currencies. " +
            "Supported currencies: USD, EUR, GBP, JPY, INR, CAD, AUD.")
    public ExchangeRate getExchangeRate(
            @ToolParam(description = "Source currency code, e.g. USD") String from,
            @ToolParam(description = "Target currency code, e.g. EUR") String to) {

        return exchangeRateService.getRate(from, to);
    }
}
