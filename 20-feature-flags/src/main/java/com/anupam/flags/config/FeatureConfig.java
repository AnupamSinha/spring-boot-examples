package com.anupam.flags.config;

import com.anupam.flags.feature.AppFeatures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.spi.FeatureProvider;

/**
 * Togglz configuration that registers the application's feature flag enum with the framework.
 * <p>
 * This configuration bean tells Togglz which enum contains the feature definitions,
 * enabling automatic state management and the admin console.
 * </p>
 *
 * @author Anupam
 */
@Configuration
public class FeatureConfig {

    /**
     * Provides the Togglz framework with the enum-based feature definitions.
     *
     * @return a {@link FeatureProvider} backed by the {@link AppFeatures} enum
     */
    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(AppFeatures.class);
    }
}
