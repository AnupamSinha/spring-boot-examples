package com.anupam.k8s.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Health and info controller for Kubernetes deployments.
 *
 * Exposes application metadata including version, pod name (injected via
 * the Kubernetes Downward API), and a timestamp. Useful for verifying
 * rolling updates and debugging pod-level routing.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${app.name:k8s-deploy}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    /** Injected from the POD_NAME environment variable via Kubernetes Downward API. */
    @Value("${POD_NAME:unknown}")
    private String podName;

    record InfoResponse(String appName, String version, String podName, String timestamp) {}

    /**
     * GET /api/info - Returns application and pod metadata.
     * Useful for verifying which pod is serving traffic during rolling updates.
     */
    @GetMapping("/info")
    public InfoResponse info() {
        return new InfoResponse(appName, appVersion, podName, Instant.now().toString());
    }
}
