package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.MatchResultDto;
import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.dto.ProfileDto;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.models.PartnerPreference;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.PartnerPreferenceRepository;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import com.eu.matrimonybackend.service.MatchService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final PartnerPreferenceRepository preferenceRepository;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    public MatchServiceImpl(PartnerPreferenceRepository preferenceRepository,
                            ProfileRepository profileRepository,
                            ModelMapper modelMapper) {
        this.preferenceRepository = preferenceRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates or replaces a profile's partner preferences (the criteria used for match scoring
     * in {@link #getTopMatchesForProfile}), upserting on the profile's existing preference row
     * if one is already present.
     *
     * @param profileId the profile whose preferences are being set
     * @param dto the desired preference values, overwriting any existing ones in full
     * @return the saved preferences, with {@code profileId} populated
     * @throws com.eu.matrimonybackend.exeptions.ResourceNotFoundException if no profile exists with this ID
     */
    @Override
    public PartnerPreferenceDto saveOrUpdatePreferences(Long profileId, PartnerPreferenceDto dto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found: " + profileId));

        PartnerPreference preference = preferenceRepository.findByProfileId(profileId)
                .orElse(new PartnerPreference());

        preference.setProfile(profile);
        preference.setMinAge(dto.getMinAge());
        preference.setMaxAge(dto.getMaxAge());
        preference.setMinHeight(dto.getMinHeight());
        preference.setMaxHeight(dto.getMaxHeight());
        preference.setPreferredGender(dto.getPreferredGender());
        preference.setPreferredCity(dto.getPreferredCity());
        preference.setPreferredCountry(dto.getPreferredCountry());
        preference.setPreferredEducation(dto.getPreferredEducation());
        preference.setPreferredMaritalStatus(dto.getPreferredMaritalStatus());

        PartnerPreference saved = preferenceRepository.save(preference);
        PartnerPreferenceDto resultDto = modelMapper.map(saved, PartnerPreferenceDto.class);
        resultDto.setProfileId(profile.getId());
        return resultDto;
    }

    @Override
    public PartnerPreferenceDto getPreferencesByProfileId(Long profileId) {
        PartnerPreference pref = preferenceRepository.findByProfileId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not configured for profile: " + profileId));
        PartnerPreferenceDto dto = modelMapper.map(pref, PartnerPreferenceDto.class);
        dto.setProfileId(profileId);
        return dto;
    }

    /**
     * Ranks every other active profile against this profile's partner preferences using
     * {@link #calculateScore}, keeping only candidates that match at least one criterion, then
     * returns the requested page of the highest-scoring results.
     *
     * <p>Scoring and sorting are done in memory over the full candidate set rather than in the
     * database, since compatibility is a weighted, multi-field calculation not easily expressed
     * as a single SQL query; the {@link Pageable} is applied afterward as an in-memory slice.
     *
     * @param profileId the profile requesting matches; excluded from its own results
     * @param pageable the requested page number and size, applied after ranking
     * @return a page of candidates sorted by descending match percentage
     * @throws com.eu.matrimonybackend.exeptions.ResourceNotFoundException if this profile has no
     *         partner preferences configured yet
     */
    @Override
    public Page<MatchResultDto> getTopMatchesForProfile(Long profileId, Pageable pageable) {
        PartnerPreference pref = preferenceRepository.findByProfileId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not configured for profile: " + profileId));

        List<Profile> allProfiles = profileRepository.findByDeletedAtIsNull();
        List<MatchResultDto> matches = new ArrayList<>();

        for (Profile candidate : allProfiles) {
            if (candidate.getId().equals(profileId)) continue; // Skip self

            double score = calculateScore(pref, candidate);
            if (score > 0) {
                ProfileDto candidateDto = modelMapper.map(candidate, ProfileDto.class);
                candidateDto.setId(candidate.getId());
                matches.add(new MatchResultDto(candidateDto, score));
            }
        }

        // Sort descending by match score
        matches.sort(Comparator.comparingDouble(MatchResultDto::getMatchPercentage).reversed());

        // Apply Pageable slicing in memory for score-ranked matches
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matches.size());

        List<MatchResultDto> pageContent = (start <= matches.size()) ? matches.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(pageContent, pageable, matches.size());
    }

    /**
     * Computes a compatibility percentage between a profile's preferences and a candidate,
     * scoring six equally-weighted criteria (gender, age range, height range, city, education,
     * marital status). Each criterion contributes 1/6th of the total only when it is both set on
     * the preference side and satisfied by the candidate; unset preference fields are skipped
     * rather than counted as a mismatch.
     *
     * @param pref the requesting profile's partner preferences
     * @param candidate the profile being scored against those preferences
     * @return a percentage from 0 to 100, rounded to the nearest whole number
     */
    private double calculateScore(PartnerPreference pref, Profile candidate) {
        double totalCriteria = 6.0;
        double matchedCriteria = 0.0;

        // 1. Gender Match
        if (pref.getPreferredGender() != null && pref.getPreferredGender().equalsIgnoreCase(candidate.getGender())) {
            matchedCriteria++;
        }
        // 2. Age Range Match
        if (candidate.getAge() != null &&
                (pref.getMinAge() == null || candidate.getAge() >= pref.getMinAge()) &&
                (pref.getMaxAge() == null || candidate.getAge() <= pref.getMaxAge())) {
            matchedCriteria++;
        }
        // 3. Height Range Match
        if (candidate.getHeight() != null &&
                (pref.getMinHeight() == null || candidate.getHeight() >= pref.getMinHeight()) &&
                (pref.getMaxHeight() == null || candidate.getHeight() <= pref.getMaxHeight())) {
            matchedCriteria++;
        }
        // 4. City / Country Match
        if (pref.getPreferredCity() != null && pref.getPreferredCity().equalsIgnoreCase(candidate.getCity())) {
            matchedCriteria++;
        }
        // 5. Education Match
        if (pref.getPreferredEducation() != null && pref.getPreferredEducation().equalsIgnoreCase(candidate.getEducation())) {
            matchedCriteria++;
        }
        // 6. Marital Status Match
        if (pref.getPreferredMaritalStatus() != null && pref.getPreferredMaritalStatus().equalsIgnoreCase(candidate.getMaritalStatus())) {
            matchedCriteria++;
        }

        return Math.round((matchedCriteria / totalCriteria) * 100.0);
    }
}