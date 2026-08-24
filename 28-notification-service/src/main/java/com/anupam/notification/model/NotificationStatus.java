package com.anupam.notification.model;

/**
 * Enum representing the lifecycle states of a notification.
 * <p>
 * Tracks whether a notification is queued, successfully sent,
 * failed permanently, or scheduled for retry.
 * </p>
 *
 * @author Anupam
 */
public enum NotificationStatus {
    /** Notification is queued for delivery. */
    QUEUED,
    /** Notification was delivered successfully. */
    SENT,
    /** Notification delivery failed after all retry attempts. */
    FAILED,
    /** Notification is scheduled for retry after a transient failure. */
    RETRY
}
