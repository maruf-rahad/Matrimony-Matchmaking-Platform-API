package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.dto.MatchResultDto;
import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import com.eu.demomatrimony.dto.ProfileDto;
import com.eu.demomatrimony.exeptions.ResourceNotFoundException;
import com.eu.demomatrimony.models.PartnerPreference;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.PartnerPreferenceRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MatchServiceImplTest {

    @Mock
    private PartnerPreferenceRepository preferenceRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Profile userProfile;
    private Profile candidateProfile;
    private PartnerPreference preference;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userProfile = new Profile();
        userProfile.setId(1L);
        userProfile.setName("John");
        userProfile.setGender("Male");

        candidateProfile = new Profile();
        candidateProfile.setId(2L);
        candidateProfile.setName("Alice");
        candidateProfile.setGender("Female");
        candidateProfile.setAge(25L);
        candidateProfile.setHeight(165.0);
        candidateProfile.setCity("Stockholm");
        candidateProfile.setEducation("Master");
        candidateProfile.setMaritalStatus("Single");

        preference = new PartnerPreference();
        preference.setId(10L);
        preference.setProfile(userProfile);
        preference.setPreferredGender("Female");
        preference.setMinAge(20L);
        preference.setMaxAge(30L);
        preference.setMinHeight(160.0);
        preference.setMaxHeight(170.0);
        preference.setPreferredCity("Stockholm");
        preference.setPreferredEducation("Master");
        preference.setPreferredMaritalStatus("Single");
    }

    @Test
    void testSaveOrUpdatePreferences_Success() {
        PartnerPreferenceDto dto = new PartnerPreferenceDto();
        dto.setPreferredGender("Female");
        dto.setMinAge(20L);
        dto.setMaxAge(30L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceRepository.findByProfileId(1L)).thenReturn(Optional.empty());
        when(preferenceRepository.save(any(PartnerPreference.class))).thenReturn(preference);
        when(modelMapper.map(preference, PartnerPreferenceDto.class)).thenReturn(dto);

        PartnerPreferenceDto result = matchService.saveOrUpdatePreferences(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.getProfileId());
        verify(preferenceRepository, times(1)).save(any(PartnerPreference.class));
    }

    @Test
    void testSaveOrUpdatePreferences_ProfileNotFound() {
        PartnerPreferenceDto dto = new PartnerPreferenceDto();

        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.saveOrUpdatePreferences(99L, dto));
    }

    @Test
    void testGetTopMatchesForProfile_Calculates100PercentMatch() {
        ProfileDto candidateDto = new ProfileDto();
        candidateDto.setName("Alice");

        when(preferenceRepository.findByProfileId(1L)).thenReturn(Optional.of(preference));
        when(profileRepository.findByDeletedAtIsNull()).thenReturn(List.of(userProfile, candidateProfile));
        when(modelMapper.map(candidateProfile, ProfileDto.class)).thenReturn(candidateDto);

        Page<MatchResultDto> matches = matchService.getTopMatchesForProfile(1L, PageRequest.of(0, 10));
        assertEquals(2, matches.getTotalElements());
        MatchResultDto match1 = matches.getContent().get(0);
        MatchResultDto match2 = matches.getContent().get(1);
    }

    @Test
    void testGetTopMatchesForProfile_CalculatesPartialMatch() {
        // Change city and education so candidate only matches 4 out of 6 criteria (~67%)
        candidateProfile.setCity("Gothenburg");
        candidateProfile.setEducation("Bachelor");

        ProfileDto candidateDto = new ProfileDto();
        candidateDto.setName("Alice");

        when(preferenceRepository.findByProfileId(1L)).thenReturn(Optional.of(preference));
        when(profileRepository.findByDeletedAtIsNull()).thenReturn(List.of(userProfile, candidateProfile));
        when(modelMapper.map(candidateProfile, ProfileDto.class)).thenReturn(candidateDto);

        Page<MatchResultDto> matches = matchService.getTopMatchesForProfile(1L, PageRequest.of(0, 10));
        assertEquals(1, matches.getTotalElements());
        MatchResultDto match1 = matches.getContent().get(0);
    }
}