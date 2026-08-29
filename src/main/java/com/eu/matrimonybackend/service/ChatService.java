package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.ChatMessageDto;
import java.util.List;

public interface ChatService {
    ChatMessageDto sendMessage(ChatMessageDto messageDto);
    List<ChatMessageDto> getChatHistory(Long senderId, Long receiverId);
}