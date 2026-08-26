package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.dto.ChatMessageDto;
import com.eu.demomatrimony.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Chat APIs", description = "Endpoints for real-time WebSockets messaging and message history")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    // WebSocket route: Clients send to /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload ChatMessageDto messageDto) {
        ChatMessageDto savedMessage = chatService.sendMessage(messageDto);

        // Push real-time payload directly to the receiver's private topic channel
        messagingTemplate.convertAndSend(
                "/topic/messages/" + savedMessage.getReceiverId(),
                savedMessage
        );
    }

    // REST endpoint to load past conversation history
    @GetMapping("/messages/{user1Id}/{user2Id}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long user1Id,
                                                               @PathVariable Long user2Id) {
        return ResponseEntity.ok(chatService.getChatHistory(user1Id, user2Id));
    }
}