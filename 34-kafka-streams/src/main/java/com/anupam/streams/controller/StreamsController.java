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

@RestController
@RequestMapping("/api/streams")
public class StreamsController {

    private final StreamsBuilderFactoryBean factoryBean;

    public StreamsController(StreamsBuilderFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    @GetMapping("/counts")
    public Map<String, Object> getPaymentCounts() {
        Map<String, Object> result = new HashMap<>();

        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        if (kafkaStreams == null) {
            result.put("error", "Kafka Streams not yet initialized");
            return result;
        }

        try {
            ReadOnlyWindowStore<String, Long> windowStore = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(
                            PaymentStreamTopology.STATE_STORE_NAME,
                            QueryableStoreTypes.windowStore()
                    )
            );

            Instant now = Instant.now();
            Instant oneMinuteAgo = now.minusSeconds(60);

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
