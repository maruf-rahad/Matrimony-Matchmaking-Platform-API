package com.eu.demomatrimony.service;

import com.eu.demomatrimony.dto.ChatMessageDto;
import java.util.List;

public interface ChatService {
    ChatMessageDto sendMessage(ChatMessageDto messageDto);
    List<ChatMessageDto> getChatHistory(Long senderId, Long receiverId);
}