package com.anupam.testcontainers.service;

import com.anupam.testcontainers.model.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);
    private final List<PaymentEvent> processedEvents = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "payments", groupId = "test-group")
    public void consume(PaymentEvent event) {
        log.info("Consumed payment event: {}", event.transactionId());
        processedEvents.add(event);
    }

    public List<PaymentEvent> getProcessedEvents() {
        return List.copyOf(processedEvents);
    }

    public void reset() {
        processedEvents.clear();
    }
}
