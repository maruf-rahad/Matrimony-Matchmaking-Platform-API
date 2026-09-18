package com.eu.matrimonybackend.dto;

public class UnreadCountsDto {
    private long unreadInterests;
    private long unreadMessages;

    public UnreadCountsDto() {}

    public UnreadCountsDto(long unreadInterests, long unreadMessages) {
        this.unreadInterests = unreadInterests;
        this.unreadMessages = unreadMessages;
    }

    public long getUnreadInterests() { return unreadInterests; }
    public void setUnreadInterests(long unreadInterests) { this.unreadInterests = unreadInterests; }

    public long getUnreadMessages() { return unreadMessages; }
    public void setUnreadMessages(long unreadMessages) { this.unreadMessages = unreadMessages; }
}
