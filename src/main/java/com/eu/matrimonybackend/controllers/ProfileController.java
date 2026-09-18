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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public List<ProfileDto> getAllProfiles(Authentication authentication) {
        return profileService.getAllVisibleProfiles(authentication.getName());
    }

    @GetMapping("/profile/{id}")
    public ProfileDto getProfileById(@PathVariable Long id, Authentication authentication) {
        return profileService.getVisibleProfileById(id, authentication.getName());
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
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(profileService.searchVisibleProfiles(criteria, pageable, authentication.getName()));
    }

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    /**
     * Uploads a new profile picture, validating its content type, storing it under
     * {@code uploads/profile-pictures} with a generated unique filename, and updating the
     * profile's stored picture URL to point at it.
     *
     * @param id the profile ID to update
     * @param file the uploaded image; must be non-empty and one of JPEG/PNG/WEBP/GIF
     * @return the updated profile, including its new {@code profilePictureUrl}
     * @throws IllegalArgumentException if the file is empty or not an accepted image type
     * @throws IOException if the file cannot be written to disk
     */
    @PostMapping("/profile/{id}/picture")
    public ProfileDto uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file was uploaded.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, WEBP, or GIF images are allowed.");
        }

        Profile existingProfile = profileService.getProfileById(id);

        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };
        String filename = "profile-" + id + "-" + UUID.randomUUID() + extension;

        Path uploadDir = Paths.get("uploads", "profile-pictures");
        Files.createDirectories(uploadDir);
        file.transferTo(uploadDir.resolve(filename));

        existingProfile.setProfilePictureUrl("/uploads/profile-pictures/" + filename);
        Profile updated = profileService.updateProfile(id, existingProfile);

        return modelMapper.map(updated, ProfileDto.class);
    }
}
