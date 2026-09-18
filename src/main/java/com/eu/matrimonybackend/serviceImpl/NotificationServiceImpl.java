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

    @Override
    public UnreadCountsDto getUnreadCounts(Long profileId) {
        long unreadInterests = interestService.countUnreadReceivedInterests(profileId);
        long unreadMessages = chatService.countUnreadMessages(profileId);
        return new UnreadCountsDto(unreadInterests, unreadMessages);
    }

    @Override
    public Map<Long, Long> getChatCountsBySender(Long profileId) {
        return chatService.countUnreadMessagesGroupedBySender(profileId);
    }
}
