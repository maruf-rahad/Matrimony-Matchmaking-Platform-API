package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.MatchResultDto;
import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    PartnerPreferenceDto saveOrUpdatePreferences(Long profileId, PartnerPreferenceDto dto);
    PartnerPreferenceDto getPreferencesByProfileId(Long profileId);
    Page<MatchResultDto> getTopMatchesForProfile(Long profileId, Pageable pageable);
}