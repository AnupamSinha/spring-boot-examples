package com.anupam.grpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * gRPC Client Application - Main Entry Point.
 *
 * Exposes REST endpoints that internally call the gRPC server.
 * Demonstrates the REST-to-gRPC gateway pattern where external clients
 * use HTTP/JSON while internal communication uses efficient gRPC/Protobuf.
 *
 * @author Anupam
 */
@SpringBootApplication
public class GrpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcClientApplication.class, args);
    }
}
