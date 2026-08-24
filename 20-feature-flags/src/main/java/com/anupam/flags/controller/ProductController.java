package com.anupam.flags.controller;

import com.anupam.flags.feature.AppFeatures;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;

import java.util.List;
import java.util.Map;

/**
 * Product controller that uses feature flags to toggle behavior at runtime.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final FeatureManager featureManager;

    public ProductController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam(defaultValue = "") String query) {

        Map<String, Object> response;

        if (featureManager.isActive(AppFeatures.PREMIUM_SEARCH)) {
            // Enhanced search — full-text, faceted, weighted scoring
            response = Map.of(
                "searchMode", "premium",
                "query", query,
                "results", premiumSearch(query),
                "facets", List.of("category", "brand", "price-range"),
                "suggestions", List.of("Did you mean: " + query + "s?")
            );
        } else {
            // Basic search — simple LIKE query
            response = Map.of(
                "searchMode", "basic",
                "query", query,
                "results", basicSearch(query)
            );
        }

        // Check if new checkout is available
        response = new java.util.HashMap<>(response);
        response.put("newCheckoutEnabled", featureManager.isActive(AppFeatures.NEW_CHECKOUT));
        response.put("darkModeEnabled", featureManager.isActive(AppFeatures.DARK_MODE));

        return ResponseEntity.ok(response);
    }

    private List<Map<String, Object>> premiumSearch(String query) {
        return List.of(
            Map.of("id", "1", "name", "Premium Result: " + query, "score", 0.98, "highlights", true),
            Map.of("id", "2", "name", "Related: " + query + " Pro", "score", 0.85, "highlights", true)
        );
    }

    private List<Map<String, Object>> basicSearch(String query) {
        return List.of(
            Map.of("id", "1", "name", "Basic Result: " + query)
        );
    }
}
