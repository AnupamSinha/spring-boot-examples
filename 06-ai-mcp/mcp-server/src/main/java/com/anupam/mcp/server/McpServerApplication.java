package com.anupam.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP Server Application - Main Entry Point.
 *
 * Exposes payment and exchange rate tools via the Model Context Protocol (MCP).
 * Any MCP-compatible client (Spring AI, Claude Desktop, etc.) can discover
 * and invoke these tools over the configured transport (stdio or SSE).
 *
 * @author Anupam
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
