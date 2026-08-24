package com.anupam.k8s.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${app.name:k8s-deploy}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${POD_NAME:unknown}")
    private String podName;

    record InfoResponse(String appName, String version, String podName, String timestamp) {}

    @GetMapping("/info")
    public InfoResponse info() {
        return new InfoResponse(appName, appVersion, podName, Instant.now().toString());
    }
}
