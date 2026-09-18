package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.InterestDto;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.models.Interest;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.InterestRepository;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import com.eu.matrimonybackend.service.InterestService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    public InterestServiceImpl(InterestRepository interestRepository, ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.interestRepository = interestRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public InterestDto sendInterest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send interest to yourself.");
        }

        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender profile not found"));
        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver profile not found"));

        Interest interest = interestRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElse(new Interest());

        interest.setSender(sender);
        interest.setReceiver(receiver);
        interest.setStatus(InterestStatus.PENDING);
        interest.setSeen(false);
        interest.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(interestRepository.save(interest), InterestDto.class);
    }

    @Override
    public InterestDto updateInterestStatus(Long interestId, InterestStatus status) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new ResourceNotFoundException("Interest request not found"));

        interest.setStatus(status);
        interest.setUpdatedAt(LocalDateTime.now());

        return modelMapper.map(interestRepository.save(interest), InterestDto.class);
    }

    @Override
    public List<InterestDto> getReceivedInterests(Long receiverId, InterestStatus status) {
        List<Interest> interests = status == null
                ? interestRepository.findByReceiverId(receiverId)
                : interestRepository.findByReceiverIdAndStatus(receiverId, status);
        return interests.stream()
                .map(interest -> modelMapper.map(interest, InterestDto.class))
                .toList();
    }

    @Override
    public List<InterestDto> getSentInterests(Long senderId, InterestStatus status) {
        List<Interest> interests = status == null
                ? interestRepository.findBySenderId(senderId)
                : interestRepository.findBySenderIdAndStatus(senderId, status);
        return interests.stream()
                .map(interest -> modelMapper.map(interest, InterestDto.class))
                .toList();
    }

    @Override
    public long countUnreadReceivedInterests(Long receiverId) {
        return interestRepository.countByReceiverIdAndStatusAndSeenFalse(receiverId, InterestStatus.PENDING);
    }

    @Override
    @Transactional
    public void markReceivedInterestsSeen(Long receiverId) {
        interestRepository.markReceivedInterestsSeen(receiverId);
    }
}