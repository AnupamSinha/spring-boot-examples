package com.anupam.streams.controller;

import com.anupam.streams.topology.PaymentStreamTopology;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller providing access to Kafka Streams state store data.
 * Exposes endpoints to query windowed payment counts by currency and
 * the current state of the Kafka Streams application.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/streams")
public class StreamsController {

    private final StreamsBuilderFactoryBean factoryBean;

    /**
     * Constructs the StreamsController with the Kafka Streams factory bean.
     *
     * @param factoryBean the Spring-managed StreamsBuilderFactoryBean
     */
    public StreamsController(StreamsBuilderFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    /**
     * Queries the windowed state store to retrieve payment counts per currency
     * within the last 60 seconds. Returns an error message if Kafka Streams
     * is not yet initialized or the state store is unavailable.
     *
     * @return a map containing the time window and payment counts by currency
     */
    @GetMapping("/counts")
    public Map<String, Object> getPaymentCounts() {
        Map<String, Object> result = new HashMap<>();

        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        if (kafkaStreams == null) {
            result.put("error", "Kafka Streams not yet initialized");
            return result;
        }

        try {
            // Access the windowed state store for payment counts
            ReadOnlyWindowStore<String, Long> windowStore = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(
                            PaymentStreamTopology.STATE_STORE_NAME,
                            QueryableStoreTypes.windowStore()
                    )
            );

            // Define the query window: last 60 seconds
            Instant now = Instant.now();
            Instant oneMinuteAgo = now.minusSeconds(60);

            // Fetch counts for each tracked currency
            Map<String, Long> counts = new HashMap<>();
            for (String currency : new String[]{"USD", "EUR", "GBP", "JPY", "INR"}) {
                try (WindowStoreIterator<Long> iterator =
                             windowStore.fetch(currency, oneMinuteAgo, now)) {
                    long total = 0;
                    while (iterator.hasNext()) {
                        total += iterator.next().value;
                    }
                    if (total > 0) {
                        counts.put(currency, total);
                    }
                }
            }

            result.put("window", Map.of("from", oneMinuteAgo.toString(), "to", now.toString()));
            result.put("counts", counts);
        } catch (Exception e) {
            result.put("error", "State store not available: " + e.getMessage());
        }

        return result;
    }

    /**
     * Returns the current state of the Kafka Streams application
     * (e.g., RUNNING, REBALANCING, NOT_INITIALIZED).
     *
     * @return a map containing the current Kafka Streams state
     */
    @GetMapping("/status")
    public Map<String, String> getStatus() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        Map<String, String> status = new HashMap<>();
        if (kafkaStreams != null) {
            status.put("state", kafkaStreams.state().name());
        } else {
            status.put("state", "NOT_INITIALIZED");
        }
        return status;
    }
}
