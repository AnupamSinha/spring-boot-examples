package com.anupam.flags.feature;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;

/**
 * Enum defining all application-level feature flags managed by Togglz.
 * <p>
 * Each constant represents a toggleable feature that can be enabled or disabled
 * at runtime. Use {@code @EnabledByDefault} to activate features by default and
 * {@code @Label} to provide a human-readable description.
 * </p>
 *
 * @author Anupam
 */
public enum AppFeatures implements Feature {

    /** Enables the new checkout flow experience. Disabled by default. */
    @Label("New Checkout Flow")
    NEW_CHECKOUT,

    /** Enables dark mode in the user interface. Enabled by default. */
    @Label("Dark Mode")
    @EnabledByDefault
    DARK_MODE,

    /** Enables premium search with full-text, faceted, and weighted scoring. Disabled by default. */
    @Label("Premium Search")
    PREMIUM_SEARCH
}
