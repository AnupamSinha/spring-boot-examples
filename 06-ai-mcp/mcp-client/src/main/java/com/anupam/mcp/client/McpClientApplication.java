package com.anupam.mcp.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP Client Application - Main Entry Point.
 *
 * Demonstrates the client side of Model Context Protocol (MCP) integration.
 * Connects to an MCP server to discover and invoke tools at runtime,
 * allowing the AI model to use remote tools without local @Tool definitions.
 *
 * @author Anupam
 */
@SpringBootApplication
public class McpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);
    }
}
