package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.ChatMessageDto;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.models.ChatMessage;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.ChatMessageRepository;
import com.eu.matrimonybackend.repositories.InterestRepository;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import com.eu.matrimonybackend.repositories.projections.UnreadCountBySender;
import com.eu.matrimonybackend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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
            log.warn("Rejected chat message: no ACCEPTED interest connection between profiles {} and {}", senderId, receiverId);
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
                saved.getTimestamp(),
                saved.isRead()
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
                        msg.getTimestamp(),
                        msg.isRead()
                ))
                .toList();
    }

    @Override
    public long countUnreadMessages(Long receiverId) {
        return chatMessageRepository.countByReceiverIdAndReadFalse(receiverId);
    }

    @Override
    public Map<Long, Long> countUnreadMessagesGroupedBySender(Long receiverId) {
        return chatMessageRepository.countUnreadGroupedBySender(receiverId).stream()
                .collect(Collectors.toMap(UnreadCountBySender::getSenderId, UnreadCountBySender::getCount));
    }

    @Override
    @Transactional
    public void markMessagesRead(Long senderId, Long receiverId) {
        chatMessageRepository.markMessagesRead(senderId, receiverId);
    }
}