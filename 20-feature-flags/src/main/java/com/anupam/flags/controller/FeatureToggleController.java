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
 * REST controller for managing feature flags at runtime.
 * <p>
 * Provides endpoints to list all features and their current state, as well as
 * enable or disable individual features. In production, these endpoints should
 * be protected with authentication and authorization.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/features")
public class FeatureToggleController {

    private final FeatureManager featureManager;

    /**
     * Constructs the controller with the Togglz feature manager.
     *
     * @param featureManager the Togglz feature manager for querying and updating feature state
     */
    public FeatureToggleController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    /**
     * Retrieves the current state of all registered feature flags.
     * <p>
     * Returns each feature's name, enabled status, and label annotation value.
     * </p>
     *
     * @return a list of feature flag details with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllFeatures() {
        var features = Arrays.stream(AppFeatures.values())
            .map(feature -> Map.<String, Object>of(
                "name", feature.name(),
                "enabled", featureManager.isActive(feature),
                // Extract the @Label annotation value if present
                "label", feature.getClass().getFields()[feature.ordinal()]
                    .isAnnotationPresent(org.togglz.core.annotation.Label.class)
                    ? feature.getClass().getFields()[feature.ordinal()]
                        .getAnnotation(org.togglz.core.annotation.Label.class).value()
                    : feature.name()
            ))
            .toList();

        return ResponseEntity.ok(features);
    }

    /**
     * Enables a specific feature flag by name.
     *
     * @param featureName the feature name (case-insensitive)
     * @return confirmation of the state change, or 400 if the feature is unknown
     */
    @PutMapping("/{featureName}/enable")
    public ResponseEntity<Map<String, Object>> enableFeature(@PathVariable String featureName) {
        return toggleFeature(featureName, true);
    }

    /**
     * Disables a specific feature flag by name.
     *
     * @param featureName the feature name (case-insensitive)
     * @return confirmation of the state change, or 400 if the feature is unknown
     */
    @PutMapping("/{featureName}/disable")
    public ResponseEntity<Map<String, Object>> disableFeature(@PathVariable String featureName) {
        return toggleFeature(featureName, false);
    }

    /**
     * Toggles a feature flag to the specified state.
     * <p>
     * Looks up the feature by name (converted to uppercase) and updates its state.
     * Returns a 400 response if the feature name is not recognized.
     * </p>
     *
     * @param featureName the feature name to toggle
     * @param enabled     the desired state (true = enabled, false = disabled)
     * @return a response map with the result or error details
     */
    private ResponseEntity<Map<String, Object>> toggleFeature(String featureName, boolean enabled) {
        try {
            // Look up the enum constant by name (case-insensitive)
            AppFeatures feature = AppFeatures.valueOf(featureName.toUpperCase());
            featureManager.setFeatureState(new FeatureState(feature, enabled));
            return ResponseEntity.ok(Map.of(
                "feature", feature.name(),
                "enabled", enabled,
                "message", "Feature " + feature.name() + (enabled ? " enabled" : " disabled")
            ));
        } catch (IllegalArgumentException e) {
            // Feature name not found in the enum
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Unknown feature: " + featureName,
                "availableFeatures", Arrays.stream(AppFeatures.values())
                    .map(Enum::name)
                    .toList()
            ));
        }
    }
}
