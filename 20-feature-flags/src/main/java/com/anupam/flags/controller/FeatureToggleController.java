package com.anupam.flags.controller;

import com.anupam.flags.feature.AppFeatures;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * REST endpoints to manage feature flags at runtime.
 * In production, protect these with authentication/authorization.
 */
@RestController
@RequestMapping("/api/features")
public class FeatureToggleController {

    private final FeatureManager featureManager;

    public FeatureToggleController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllFeatures() {
        var features = Arrays.stream(AppFeatures.values())
            .map(feature -> Map.<String, Object>of(
                "name", feature.name(),
                "enabled", featureManager.isActive(feature),
                "label", feature.getClass().getFields()[feature.ordinal()]
                    .isAnnotationPresent(org.togglz.core.annotation.Label.class)
                    ? feature.getClass().getFields()[feature.ordinal()]
                        .getAnnotation(org.togglz.core.annotation.Label.class).value()
                    : feature.name()
            ))
            .toList();

        return ResponseEntity.ok(features);
    }

    @PutMapping("/{featureName}/enable")
    public ResponseEntity<Map<String, Object>> enableFeature(@PathVariable String featureName) {
        return toggleFeature(featureName, true);
    }

    @PutMapping("/{featureName}/disable")
    public ResponseEntity<Map<String, Object>> disableFeature(@PathVariable String featureName) {
        return toggleFeature(featureName, false);
    }

    private ResponseEntity<Map<String, Object>> toggleFeature(String featureName, boolean enabled) {
        try {
            AppFeatures feature = AppFeatures.valueOf(featureName.toUpperCase());
            featureManager.setFeatureState(new FeatureState(feature, enabled));
            return ResponseEntity.ok(Map.of(
                "feature", feature.name(),
                "enabled", enabled,
                "message", "Feature " + feature.name() + (enabled ? " enabled" : " disabled")
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Unknown feature: " + featureName,
                "availableFeatures", Arrays.stream(AppFeatures.values())
                    .map(Enum::name)
                    .toList()
            ));
        }
    }
}
