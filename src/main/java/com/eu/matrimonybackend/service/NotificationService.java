package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.UnreadCountsDto;

import java.util.Map;

public interface NotificationService {
    UnreadCountsDto getUnreadCounts(Long profileId);

    /** senderId -> unread message count from that sender, for the given profile. */
    Map<Long, Long> getChatCountsBySender(Long profileId);
}
