package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.dto.ProfileDto;
import com.eu.matrimonybackend.service.ProfileService;
import com.eu.matrimonybackend.models.Profile;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Profile Apis", description = "create, get, update, delete")
public class ProfileController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;

    public ProfileController(ProfileService profileService, ModelMapper modelMapper) {
        this.profileService = profileService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/profile")
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();
        profiles = profileService.getAllProfiles();
        return profiles.stream()
                .map(
                        profile -> modelMapper.map(profile, ProfileDto.class)
                ).toList();
    }

    @GetMapping("/profile/{id}")
    public ProfileDto getProfileById(@PathVariable Long id) {

        return modelMapper.map(
                profileService.getProfileById(id), ProfileDto.class
        );
    }

    @PostMapping("/profile")
    public ProfileDto addProfile(@RequestBody ProfileDto profileDto) {
        Profile profile = modelMapper.map(profileDto, Profile.class);

        return modelMapper.map(
                profileService.createProfile(profile), ProfileDto.class
        );
    }

    @PutMapping("/profile/{id}")
    public ProfileDto updateProfile(@PathVariable Long id, @RequestBody ProfileDto profileDto) {
        Profile profile = modelMapper.map(profileDto, Profile.class);
        return modelMapper.map(
                profileService.updateProfile(id, profile), ProfileDto.class
        );
    }

    @DeleteMapping("/profile/{id}")
    public HttpStatus deleteProfile(@PathVariable Long id) {
        profileService.deleteProfileById(id);

        return HttpStatus.OK;
    }

    @PostMapping("/profile/search")
    public ResponseEntity<Page<ProfileDto>> searchProfiles(
            @RequestBody PartnerPreferenceDto criteria,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(profileService.searchProfiles(criteria, pageable));
    }
}
