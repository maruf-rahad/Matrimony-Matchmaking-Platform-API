package com.eu.matrimonybackend.dto;

import com.eu.matrimonybackend.enums.InterestStatus;
import lombok.Data;

@Data
public class InterestDto {
    private Long id;
    private Long senderId;
    private Long receiverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public InterestStatus getStatus() {
        return status;
    }

    public void setStatus(InterestStatus status) {
        this.status = status;
    }

    private InterestStatus status;
}