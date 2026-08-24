package com.anupam.order.model;

import com.anupam.events.OrderItem;
import java.util.List;

public record CreateOrderRequest(String customerId, List<OrderItem> items) {}
