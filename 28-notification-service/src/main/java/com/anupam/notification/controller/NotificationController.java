package com.anupam.notification.controller;

import com.anupam.notification.model.NotificationRequest;
import com.anupam.notification.model.NotificationStatus;
import com.anupam.notification.producer.NotificationProducer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for submitting notification requests.
 * <p>
 * Accepts notification requests via HTTP, validates them, and publishes
 * them to Kafka for asynchronous processing. Returns immediately with
 * a 202 Accepted status indicating the notification has been queued.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationProducer notificationProducer;

    /**
     * Constructs the controller with the Kafka notification producer.
     *
     * @param notificationProducer the producer that publishes messages to Kafka
     */
    public NotificationController(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    /**
     * Accepts a notification request and queues it for asynchronous delivery.
     *
     * @param request the validated notification request
     * @return a 202 Accepted response with queue confirmation details
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> send(@Valid @RequestBody NotificationRequest request) {
        // Publish the notification to Kafka for async processing
        notificationProducer.send(request);

        Map<String, Object> response = Map.of(
                "status", NotificationStatus.QUEUED,
                "message", "Notification queued for delivery",
                "channel", request.channel(),
                "recipient", request.recipient()
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
