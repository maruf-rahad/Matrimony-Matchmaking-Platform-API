package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.UnreadCountsDto;
import com.eu.matrimonybackend.service.ChatService;
import com.eu.matrimonybackend.service.InterestService;
import com.eu.matrimonybackend.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final InterestService interestService;
    private final ChatService chatService;

    public NotificationServiceImpl(InterestService interestService, ChatService chatService) {
        this.interestService = interestService;
        this.chatService = chatService;
    }

    /**
     * Aggregates the two notification badge counts a client needs on login or reconnect: unseen
     * pending interest requests and unread chat messages, across all senders combined.
     *
     * @param profileId the profile to compute counts for
     * @return the combined unread interest and chat message counts
     */
    @Override
    public UnreadCountsDto getUnreadCounts(Long profileId) {
        long unreadInterests = interestService.countUnreadReceivedInterests(profileId);
        long unreadMessages = chatService.countUnreadMessages(profileId);
        return new UnreadCountsDto(unreadInterests, unreadMessages);
    }

    /**
     * Breaks down a profile's unread chat messages by sender, so the client can badge each
     * conversation in a chat list individually rather than only showing one combined total.
     *
     * @param profileId the profile to compute per-sender counts for
     * @return a map of sender profile ID to that sender's unread message count
     */
    @Override
    public Map<Long, Long> getChatCountsBySender(Long profileId) {
        return chatService.countUnreadMessagesGroupedBySender(profileId);
    }
}
