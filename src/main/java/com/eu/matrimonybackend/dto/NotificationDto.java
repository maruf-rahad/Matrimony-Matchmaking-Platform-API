package com.eu.matrimonybackend.dto;

import com.eu.matrimonybackend.enums.NotificationType;

import java.time.LocalDateTime;

/** Payload pushed to a recipient's personal /topic/notifications/{recipientId} channel. */
public class NotificationDto {
    private NotificationType type;
    private Long senderId;
    private Long receiverId;
    private Long referenceId;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public NotificationDto(NotificationType type, Long senderId, Long receiverId, Long referenceId, LocalDateTime createdAt) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
    }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
