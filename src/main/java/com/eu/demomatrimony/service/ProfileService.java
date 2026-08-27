package com.eu.demomatrimony.service;


import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import com.eu.demomatrimony.dto.ProfileDto;
import com.eu.demomatrimony.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileService {

    public List<Profile> getAllProfiles();

    public Profile getProfileById(Long id);

    public Profile createProfile(Profile profile);

    public Profile updateProfile(Long id, Profile profile);

    public void deleteProfileById(Long id);

    Page<ProfileDto> searchProfiles(PartnerPreferenceDto criteria, Pageable pageable);}
