package com.eu.demomatrimony.service;

import com.eu.demomatrimony.dto.MatchResultDto;
import com.eu.demomatrimony.dto.PartnerPreferenceDto;

import java.util.List;

public interface MatchService {
    PartnerPreferenceDto saveOrUpdatePreferences(Long profileId, PartnerPreferenceDto dto);
    PartnerPreferenceDto getPreferencesByProfileId(Long profileId);
    List<MatchResultDto> getTopMatchesForProfile(Long profileId);
}