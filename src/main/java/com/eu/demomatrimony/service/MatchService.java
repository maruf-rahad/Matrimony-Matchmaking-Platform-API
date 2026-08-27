package com.eu.demomatrimony.service;

import com.eu.demomatrimony.dto.MatchResultDto;
import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    PartnerPreferenceDto saveOrUpdatePreferences(Long profileId, PartnerPreferenceDto dto);
    PartnerPreferenceDto getPreferencesByProfileId(Long profileId);
    Page<MatchResultDto> getTopMatchesForProfile(Long profileId, Pageable pageable);
}