package com.anupam.mcp.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRate(
        String from,
        String to,
        BigDecimal rate,
        LocalDateTime timestamp
) {}
