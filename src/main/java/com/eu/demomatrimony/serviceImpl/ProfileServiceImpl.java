package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import com.eu.demomatrimony.dto.ProfileDto;
import com.eu.demomatrimony.exeptions.ResourceNotFoundException;
import com.eu.demomatrimony.service.ProfileService;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.ProfileRepository;
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
    private final ModelMapper modelMapper; // <--- 1. Declare modelMapper field

    public ProfileServiceImpl(ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
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
}
