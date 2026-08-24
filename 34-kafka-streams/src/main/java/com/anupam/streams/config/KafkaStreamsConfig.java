package com.anupam.streams.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultProperties;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Apache Kafka Streams.
 * Defines the default streams configuration bean including application ID,
 * bootstrap servers, default serializers/deserializers, state directory,
 * and commit interval settings.
 *
 * @author Anupam
 */
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    /**
     * Creates the default Kafka Streams configuration bean used by Spring Kafka
     * to initialize the StreamsBuilder factory.
     *
     * @return the Kafka Streams configuration with all required properties
     */
    @Bean(name = KafkaStreamsDefaultProperties.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap<>();
        // Unique application ID for this streams instance
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        // Kafka broker connection endpoints
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Default key and value serializers using String serde
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        // Local state store directory for changelog topics
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        // Commit processed offsets every 1 second
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        return new KafkaStreamsConfiguration(props);
    }
}
