package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.InterestDto;
import com.eu.matrimonybackend.enums.InterestStatus;

import java.util.List;

public interface InterestService {
    InterestDto sendInterest(Long senderId, Long receiverId);
    InterestDto updateInterestStatus(Long interestId, InterestStatus status);
    List<InterestDto> getReceivedInterests(Long receiverId, InterestStatus status);
}