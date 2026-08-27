package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.dto.ChatMessageDto;
import com.eu.demomatrimony.enums.InterestStatus;
import com.eu.demomatrimony.models.ChatMessage;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.ChatMessageRepository;
import com.eu.demomatrimony.repositories.InterestRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    void sendMessage_ShouldThrowException_WhenNoAcceptedInterestExists() {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        when(interestRepository.existsBySenderIdAndReceiverIdAndStatus(1L, 2L, InterestStatus.ACCEPTED))
                .thenReturn(false);
        when(interestRepository.existsBySenderIdAndReceiverIdAndStatus(2L, 1L, InterestStatus.ACCEPTED))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> chatService.sendMessage(dto));
        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
    }

    @Test
    void sendMessage_ShouldSaveMessage_WhenAcceptedInterestExists() {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        Profile senderProfile = new Profile();
        senderProfile.setId(1L);

        Profile receiverProfile = new Profile();
        receiverProfile.setId(2L);

        ChatMessage savedEntity = new ChatMessage();
        savedEntity.setId(100L);
        savedEntity.setSender(senderProfile);
        savedEntity.setReceiver(receiverProfile);

        when(interestRepository.existsBySenderIdAndReceiverIdAndStatus(1L, 2L, InterestStatus.ACCEPTED))
                .thenReturn(true);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(senderProfile));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(receiverProfile));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(savedEntity);

        chatService.sendMessage(dto);

        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }
}