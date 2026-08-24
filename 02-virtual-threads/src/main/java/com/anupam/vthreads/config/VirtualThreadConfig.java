package com.anupam.vthreads.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configures Tomcat to use virtual threads for handling HTTP requests.
 *
 * By replacing Tomcat's default platform thread pool with a virtual-thread-per-task
 * executor, every incoming request is handled on a lightweight virtual thread.
 * This dramatically improves throughput for I/O-bound workloads without
 * increasing memory consumption.
 *
 * @author Anupam
 */
@Configuration
public class VirtualThreadConfig {

    /**
     * Customizes the Tomcat protocol handler to use a virtual thread executor.
     * Each incoming request spawns a new virtual thread instead of blocking a platform thread.
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
