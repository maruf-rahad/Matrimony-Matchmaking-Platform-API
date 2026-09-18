package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.ChatMessageDto;
import java.util.List;
import java.util.Map;

public interface ChatService {
    ChatMessageDto sendMessage(ChatMessageDto messageDto);
    List<ChatMessageDto> getChatHistory(Long senderId, Long receiverId);

    long countUnreadMessages(Long receiverId);

    /** senderId -> count of unread messages from that sender to receiverId. */
    Map<Long, Long> countUnreadMessagesGroupedBySender(Long receiverId);

    void markMessagesRead(Long senderId, Long receiverId);
}