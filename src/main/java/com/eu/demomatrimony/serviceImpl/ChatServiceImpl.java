package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.dto.ChatMessageDto;
import com.eu.demomatrimony.exeptions.ResourceNotFoundException;
import com.eu.demomatrimony.models.ChatMessage;
import com.eu.demomatrimony.enums.InterestStatus;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.ChatMessageRepository;
import com.eu.demomatrimony.repositories.InterestRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
import com.eu.demomatrimony.service.ChatService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository,
                           ProfileRepository profileRepository,
                           InterestRepository interestRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.profileRepository = profileRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    public ChatMessageDto sendMessage(ChatMessageDto messageDto) {
        Long senderId = messageDto.getSenderId();
        Long receiverId = messageDto.getReceiverId();

        // 1. Verify ACCEPTED interest status between users in either direction
        boolean isConnected = interestRepository.existsBySenderIdAndReceiverIdAndStatus(senderId, receiverId, InterestStatus.ACCEPTED) ||
                interestRepository.existsBySenderIdAndReceiverIdAndStatus(receiverId, senderId, InterestStatus.ACCEPTED);

        if (!isConnected) {
            throw new IllegalArgumentException("Cannot send message. You must have an ACCEPTED interest connection with this profile.");
        }

        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender profile not found: " + senderId));
        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver profile not found: " + receiverId));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDto.getContent());
        message.setTimestamp(LocalDateTime.now());

        ChatMessage saved = chatMessageRepository.save(message);

        return new ChatMessageDto(
                saved.getId(),
                saved.getSender().getId(),
                saved.getReceiver().getId(),
                saved.getContent(),
                saved.getTimestamp()
        );
    }

    @Override
    public List<ChatMessageDto> getChatHistory(Long senderId, Long receiverId) {
        return chatMessageRepository.findChatHistory(senderId, receiverId).stream()
                .map(msg -> new ChatMessageDto(
                        msg.getId(),
                        msg.getSender().getId(),
                        msg.getReceiver().getId(),
                        msg.getContent(),
                        msg.getTimestamp()
                ))
                .toList();
    }
}