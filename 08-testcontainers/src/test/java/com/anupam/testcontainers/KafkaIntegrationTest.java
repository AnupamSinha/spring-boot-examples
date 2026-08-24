package com.anupam.testcontainers;

import com.anupam.testcontainers.model.PaymentEvent;
import com.anupam.testcontainers.service.PaymentEventConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Integration test for Kafka producer/consumer using a real Kafka container.
 */
@SpringBootTest
@Testcontainers
class KafkaIntegrationTest {

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @Container
    @ServiceConnection
    static org.testcontainers.containers.PostgreSQLContainer<?> postgres =
            new org.testcontainers.containers.PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    private PaymentEventConsumer consumer;

    @Test
    void shouldPublishAndConsumePaymentEvent() throws Exception {
        consumer.reset();

        PaymentEvent event = new PaymentEvent("TXN-100", "COMPLETED", new BigDecimal("99.99"));
        kafkaTemplate.send("payments", event.transactionId(), event).get();

        await().atMost(Duration.ofSeconds(15))
                .untilAsserted(() ->
                        assertThat(consumer.getProcessedEvents())
                                .anyMatch(e -> e.transactionId().equals("TXN-100")));
    }

    @Test
    void shouldConsumeMultipleEvents() throws Exception {
        consumer.reset();

        kafkaTemplate.send("payments", "TXN-201", new PaymentEvent("TXN-201", "COMPLETED", BigDecimal.TEN)).get();
        kafkaTemplate.send("payments", "TXN-202", new PaymentEvent("TXN-202", "FAILED", BigDecimal.ONE)).get();

        await().atMost(Duration.ofSeconds(15))
                .untilAsserted(() ->
                        assertThat(consumer.getProcessedEvents()).hasSizeGreaterThanOrEqualTo(2));
    }
}
