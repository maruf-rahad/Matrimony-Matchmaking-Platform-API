package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.dto.InterestDto;
import com.eu.demomatrimony.exeptions.ResourceNotFoundException;
import com.eu.demomatrimony.models.Interest;
import com.eu.demomatrimony.enums.InterestStatus;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.InterestRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
import com.eu.demomatrimony.service.InterestService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        return interestRepository.findByReceiverIdAndStatus(receiverId, status)
                .stream()
                .map(interest -> modelMapper.map(interest, InterestDto.class))
                .toList();
    }
}