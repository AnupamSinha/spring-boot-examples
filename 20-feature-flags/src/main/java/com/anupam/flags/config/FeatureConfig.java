package com.anupam.flags.config;

import com.anupam.flags.feature.AppFeatures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.spi.FeatureProvider;

/**
 * Togglz configuration — registers the feature enum with the framework.
 */
@Configuration
public class FeatureConfig {

    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(AppFeatures.class);
    }
}
