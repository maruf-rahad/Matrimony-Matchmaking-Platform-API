package com.eu.matrimonybackend.service;


import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.dto.ProfileDto;
import com.eu.matrimonybackend.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileService {

    public List<Profile> getAllProfiles();

    public Profile getProfileById(Long id);

    public Profile createProfile(Profile profile);

    public Profile updateProfile(Long id, Profile profile);

    public void deleteProfileById(Long id);

    Page<ProfileDto> searchProfiles(PartnerPreferenceDto criteria, Pageable pageable);

    /**
     * Read APIs exposed to end users must go through these "visible" variants so that
     * private fields (phone, email, birthday, parents' names/occupations) are hidden
     * from viewers who aren't the profile owner and don't have an ACCEPTED interest
     * connection with them. Internal/admin use should keep using the plain methods above.
     */
    ProfileDto getVisibleProfileById(Long targetId, String viewerEmail);

    List<ProfileDto> getAllVisibleProfiles(String viewerEmail);

    Page<ProfileDto> searchVisibleProfiles(PartnerPreferenceDto criteria, Pageable pageable, String viewerEmail);
}
