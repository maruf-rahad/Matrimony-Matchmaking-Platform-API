package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.InterestDto;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.models.Interest;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.InterestRepository;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InterestServiceImplTest {

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InterestServiceImpl interestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendInterest_Success() {
        Profile sender = new Profile();
        sender.setId(1L);

        Profile receiver = new Profile();
        receiver.setId(2L);

        Interest savedInterest = new Interest();
        savedInterest.setId(10L);
        savedInterest.setSender(sender);
        savedInterest.setReceiver(receiver);
        savedInterest.setStatus(InterestStatus.PENDING);

        InterestDto expectedDto = new InterestDto();
        expectedDto.setId(10L);
        expectedDto.setSenderId(1L);
        expectedDto.setReceiverId(2L);
        expectedDto.setStatus(InterestStatus.PENDING);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(interestRepository.findBySenderIdAndReceiverId(1L, 2L)).thenReturn(Optional.empty());
        when(interestRepository.save(any(Interest.class))).thenReturn(savedInterest);
        when(modelMapper.map(savedInterest, InterestDto.class)).thenReturn(expectedDto);

        InterestDto result = interestService.sendInterest(1L, 2L);

        assertNotNull(result);
        assertEquals(InterestStatus.PENDING, result.getStatus());
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getReceiverId());

        verify(interestRepository, times(1)).save(any(Interest.class));
    }

    @Test
    void testSendInterest_SameUser_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> interestService.sendInterest(1L, 1L)
        );

        assertEquals("Cannot send interest to yourself.", exception.getMessage());
        verifyNoInteractions(interestRepository);
    }

    @Test
    void testUpdateInterestStatus_NotFound() {
        when(interestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> interestService.updateInterestStatus(99L, InterestStatus.ACCEPTED));
    }
}