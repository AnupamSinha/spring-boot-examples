package com.anupam.testcontainers.service;

import com.anupam.testcontainers.model.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kafka consumer that processes payment events from the "payments" topic.
 *
 * Stores consumed events in a thread-safe list for integration test verification.
 * The reset() method allows tests to clear state between test cases.
 *
 * @author Anupam
 */
@Service
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    /** Thread-safe list of processed events (for test assertions). */
    private final List<PaymentEvent> processedEvents = Collections.synchronizedList(new ArrayList<>());

    /** Consumes payment events and stores them for verification. */
    @KafkaListener(topics = "payments", groupId = "test-group")
    public void consume(PaymentEvent event) {
        log.info("Consumed payment event: {}", event.transactionId());
        processedEvents.add(event);
    }

    /** Returns an immutable copy of all processed events. */
    public List<PaymentEvent> getProcessedEvents() {
        return List.copyOf(processedEvents);
    }

    /** Clears the processed events list (used between test cases). */
    public void reset() {
        processedEvents.clear();
    }
}
