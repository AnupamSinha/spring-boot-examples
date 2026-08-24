package com.anupam.flags.feature;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;

/**
 * Feature flags enum — each constant represents a toggleable feature.
 * Togglz uses this enum to manage feature states.
 */
public enum AppFeatures implements Feature {

    @Label("New Checkout Flow")
    NEW_CHECKOUT,

    @Label("Dark Mode")
    @EnabledByDefault
    DARK_MODE,

    @Label("Premium Search")
    PREMIUM_SEARCH
}
