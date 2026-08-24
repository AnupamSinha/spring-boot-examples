package com.anupam.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA Auditing support.
 *
 * When enabled, @CreatedDate and @LastModifiedDate annotations on entity fields
 * are automatically populated by Spring Data during persist and update operations.
 *
 * @author Anupam
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
}
