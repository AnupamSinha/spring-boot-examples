package com.anupam.native_app.config;

import com.anupam.native_app.model.Greeting;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Registers GraalVM native image runtime hints for reflection and resource access.
 *
 * GraalVM performs ahead-of-time (AOT) compilation and requires explicit hints
 * for any runtime reflection or resource loading. This class ensures the Greeting
 * record is accessible via reflection in the native image.
 *
 * @author Anupam
 */
@Configuration
@ImportRuntimeHints(NativeHints.AppRuntimeHints.class)
public class NativeHints {

    /**
     * Custom RuntimeHintsRegistrar that tells GraalVM which classes
     * need reflection access and which resources should be included.
     */
    static class AppRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // Register reflection hints for the Greeting record so it can be
            // serialized to JSON and accessed via reflection endpoints
            hints.reflection().registerType(Greeting.class,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.DECLARED_FIELDS);

            // Register resource hints for native image metadata files
            hints.resources().registerPattern("META-INF/native-image/*");
        }
    }
}
