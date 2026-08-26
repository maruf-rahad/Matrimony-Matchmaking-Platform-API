package com.eu.demomatrimony.service;

import com.eu.demomatrimony.dto.InterestDto;
import com.eu.demomatrimony.enums.InterestStatus;

import java.util.List;

public interface InterestService {
    InterestDto sendInterest(Long senderId, Long receiverId);
    InterestDto updateInterestStatus(Long interestId, InterestStatus status);
    List<InterestDto> getReceivedInterests(Long receiverId, InterestStatus status);
}