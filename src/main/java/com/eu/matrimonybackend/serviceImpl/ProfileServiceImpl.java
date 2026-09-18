package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.dto.ProfileDto;
import com.eu.matrimonybackend.enums.InterestStatus;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.repositories.InterestRepository;
import com.eu.matrimonybackend.service.ProfileService;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;
    private final ModelMapper modelMapper; // <--- 1. Declare modelMapper field

    public ProfileServiceImpl(ProfileRepository profileRepository, InterestRepository interestRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.interestRepository = interestRepository;
        this.modelMapper = modelMapper;
    }
    public List<Profile> getAllProfiles() {
        System.out.println("getAllProfiles");
        return profileRepository.findAll();
    }

    @Override
    public Profile getProfileById(Long id) {
        return getById(id);
    }

    @Override
    public Profile createProfile(Profile profile) {
        System.out.println(profile);
        profileRepository.save(profile);
        return profile;
    }

    @Override
    public Profile updateProfile(Long id, Profile newProfile) {
        Profile existingProfile = getById(id);
        BeanUtils.copyProperties(newProfile, existingProfile, "id");
        profileRepository.save(existingProfile);
        return existingProfile;
    }

    @Override
    public void deleteProfileById(Long id) {
        Profile profile = getById(id);

        profileRepository.deleteById(profile.getId());
    }

    private Profile getById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
    }

    /**
     * Filters profiles by partner-preference criteria, treating any {@code null} or blank
     * criteria field as "no filter" for that attribute rather than excluding every profile.
     * Callers needing viewer-aware privacy masking should use {@link #searchVisibleProfiles}
     * instead, since this method returns unmasked data.
     *
     * @param criteria the filter values (gender, age range, city, education, marital status);
     *                 unset fields are ignored
     * @param pageable the requested page number, size, and sort order
     * @return a page of matching profiles mapped to {@link ProfileDto}
     */
    @Override
    public Page<ProfileDto> searchProfiles(PartnerPreferenceDto criteria, Pageable pageable) {
        Page<Profile> profilesPage = profileRepository.searchProfiles(
                criteria.getPreferredGender(),
                criteria.getMinAge(),
                criteria.getMaxAge(),
                criteria.getPreferredCity(),
                criteria.getPreferredEducation(),
                criteria.getPreferredMaritalStatus(),
                pageable
        );

        List<ProfileDto> dtos = profilesPage.getContent().stream()
                .map(profile -> modelMapper.map(profile, ProfileDto.class))
                .toList();

        return new PageImpl<>(dtos, pageable, profilesPage.getTotalElements());
    }

    /**
     * Fetches a single profile as it should appear to a specific viewer, hiding private fields
     * (phone, email, birthday, parents' names/occupations) unless the viewer owns the profile or
     * has an ACCEPTED interest connection with it.
     *
     * @param targetId the ID of the profile being viewed
     * @param viewerEmail the login email of the requesting user, used to resolve their own
     *                     profile and relationship to the target; may be {@code null} for an
     *                     unauthenticated caller, in which case the profile is always masked
     * @return the target profile, with private fields nulled out unless visibility is earned
     * @throws com.eu.matrimonybackend.exeptions.ResourceNotFoundException if no profile exists with this ID
     */
    @Override
    public ProfileDto getVisibleProfileById(Long targetId, String viewerEmail) {
        Profile target = getById(targetId);
        ProfileDto dto = modelMapper.map(target, ProfileDto.class);
        Long viewerProfileId = resolveViewerProfileId(viewerEmail);
        if (!isVisibleWithoutMasking(viewerProfileId, target.getId())) {
            maskPrivateFields(dto);
        }
        return dto;
    }

    /**
     * Lists every profile as it should appear to a specific viewer, applying the same
     * per-profile private-field masking as {@link #getVisibleProfileById}.
     *
     * @param viewerEmail the login email of the requesting user; may be {@code null}, in which
     *                     case every profile is masked
     * @return every profile, each individually masked or unmasked based on the viewer's
     *         relationship to it
     */
    @Override
    public List<ProfileDto> getAllVisibleProfiles(String viewerEmail) {
        Long viewerProfileId = resolveViewerProfileId(viewerEmail);
        return getAllProfiles().stream()
                .map(profile -> {
                    ProfileDto dto = modelMapper.map(profile, ProfileDto.class);
                    if (!isVisibleWithoutMasking(viewerProfileId, profile.getId())) {
                        maskPrivateFields(dto);
                    }
                    return dto;
                })
                .toList();
    }

    /**
     * Runs {@link #searchProfiles} and then applies the same private-field masking as
     * {@link #getVisibleProfileById} to every result, so search results respect the viewer's
     * relationship to each candidate.
     *
     * @param criteria the filter values, as in {@link #searchProfiles}
     * @param pageable the requested page number, size, and sort order
     * @param viewerEmail the login email of the requesting user; may be {@code null}, in which
     *                     case every result is masked
     * @return a page of matching profiles, each individually masked or unmasked
     */
    @Override
    public Page<ProfileDto> searchVisibleProfiles(PartnerPreferenceDto criteria, Pageable pageable, String viewerEmail) {
        Page<ProfileDto> page = searchProfiles(criteria, pageable);
        Long viewerProfileId = resolveViewerProfileId(viewerEmail);
        List<ProfileDto> masked = page.getContent().stream()
                .peek(dto -> {
                    if (!isVisibleWithoutMasking(viewerProfileId, dto.getId())) {
                        maskPrivateFields(dto);
                    }
                })
                .toList();
        return new PageImpl<>(masked, pageable, page.getTotalElements());
    }

    private Long resolveViewerProfileId(String viewerEmail) {
        if (viewerEmail == null) return null;
        return profileRepository.findByEmail(viewerEmail).map(Profile::getId).orElse(null);
    }

    private boolean isVisibleWithoutMasking(Long viewerProfileId, Long targetProfileId) {
        if (viewerProfileId == null || targetProfileId == null) return false;
        if (viewerProfileId.equals(targetProfileId)) return true;
        return interestRepository.existsBySenderIdAndReceiverIdAndStatus(viewerProfileId, targetProfileId, InterestStatus.ACCEPTED)
                || interestRepository.existsBySenderIdAndReceiverIdAndStatus(targetProfileId, viewerProfileId, InterestStatus.ACCEPTED);
    }

    private void maskPrivateFields(ProfileDto dto) {
        dto.setPhone(null);
        dto.setEmail(null);
        dto.setBirthday(null);
        dto.setFatherName(null);
        dto.setFatherOccupation(null);
        dto.setMotherName(null);
        dto.setMotherOccupation(null);
    }
}
