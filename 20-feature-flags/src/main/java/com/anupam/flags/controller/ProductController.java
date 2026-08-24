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
 * Product controller that demonstrates feature-flag-driven behavior.
 * <p>
 * Uses Togglz feature flags to toggle between basic and premium search modes
 * at runtime without code changes or redeployment.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final FeatureManager featureManager;

    /**
     * Constructs the controller with the Togglz feature manager.
     *
     * @param featureManager the feature manager for checking feature states
     */
    public ProductController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    /**
     * Searches products using either premium or basic search based on the PREMIUM_SEARCH flag.
     * <p>
     * When PREMIUM_SEARCH is active, returns full-text results with facets and suggestions.
     * Otherwise, returns simple LIKE-based results. Also includes the current state of
     * NEW_CHECKOUT and DARK_MODE flags in the response.
     * </p>
     *
     * @param query the search query string; defaults to empty
     * @return a response map containing search mode, results, and active feature states
     */
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

        // Enrich response with other feature flag states
        response = new java.util.HashMap<>(response);
        response.put("newCheckoutEnabled", featureManager.isActive(AppFeatures.NEW_CHECKOUT));
        response.put("darkModeEnabled", featureManager.isActive(AppFeatures.DARK_MODE));

        return ResponseEntity.ok(response);
    }

    /**
     * Simulates a premium search with weighted scoring and highlights.
     *
     * @param query the search query
     * @return a list of premium search result maps
     */
    private List<Map<String, Object>> premiumSearch(String query) {
        return List.of(
            Map.of("id", "1", "name", "Premium Result: " + query, "score", 0.98, "highlights", true),
            Map.of("id", "2", "name", "Related: " + query + " Pro", "score", 0.85, "highlights", true)
        );
    }

    /**
     * Simulates a basic search returning simple results.
     *
     * @param query the search query
     * @return a list of basic search result maps
     */
    private List<Map<String, Object>> basicSearch(String query) {
        return List.of(
            Map.of("id", "1", "name", "Basic Result: " + query)
        );
    }
}
