package com.anupam.grpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * gRPC Server Application - Main Entry Point.
 *
 * Runs a gRPC server that exposes payment operations (unary and server-streaming).
 * Uses grpc-spring-boot-starter for auto-configuration of the gRPC server
 * alongside the standard Spring Boot web server.
 *
 * @author Anupam
 */
@SpringBootApplication
public class GrpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcServerApplication.class, args);
    }
}
