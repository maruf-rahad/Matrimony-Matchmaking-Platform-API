package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.AdminProfileDetailDto;
import com.eu.matrimonybackend.dto.AdminProfileSummaryDto;
import com.eu.matrimonybackend.dto.AdminStatsDto;
import com.eu.matrimonybackend.dto.AdminUserDto;
import com.eu.matrimonybackend.dto.CreateAdminRequestDto;
import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.enums.Role;
import com.eu.matrimonybackend.exeptions.ResourceNotFoundException;
import com.eu.matrimonybackend.models.PartnerPreference;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.models.User;
import com.eu.matrimonybackend.repositories.ChatMessageRepository;
import com.eu.matrimonybackend.repositories.InterestRepository;
import com.eu.matrimonybackend.repositories.PartnerPreferenceRepository;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import com.eu.matrimonybackend.repositories.UserRepository;
import com.eu.matrimonybackend.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PartnerPreferenceRepository partnerPreferenceRepository;
    private final InterestRepository interestRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AdminServiceImpl(UserRepository userRepository,
                             ProfileRepository profileRepository,
                             PartnerPreferenceRepository partnerPreferenceRepository,
                             InterestRepository interestRepository,
                             ChatMessageRepository chatMessageRepository,
                             PasswordEncoder passwordEncoder,
                             ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.partnerPreferenceRepository = partnerPreferenceRepository;
        this.interestRepository = interestRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public AdminStatsDto getStats() {
        long totalProfiles = profileRepository.count();
        long totalAdmins = userRepository.countByRolesContaining(Role.ROLE_ADMIN);
        return new AdminStatsDto(totalProfiles, totalAdmins);
    }

    @Override
    public List<AdminProfileSummaryDto> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::toSummaryDto)
                .toList();
    }

    @Override
    public AdminProfileDetailDto getProfileById(Long id) {
        Profile profile = getProfileOrThrow(id);
        return toDetailDto(profile);
    }

    @Override
    public void deleteProfile(Long id) {
        Profile profile = getProfileOrThrow(id);

        chatMessageRepository.deleteAll(chatMessageRepository.findBySenderIdOrReceiverId(id, id));
        interestRepository.deleteAll(interestRepository.findBySenderIdOrReceiverId(id, id));
        partnerPreferenceRepository.findByProfileId(id).ifPresent(partnerPreferenceRepository::delete);

        userRepository.findByProfileId(id)
                .ifPresentOrElse(userRepository::delete, () -> profileRepository.delete(profile));
    }

    @Override
    public AdminUserDto createAdmin(CreateAdminRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user != null) {
            Set<Role> roles = new HashSet<>(user.getRoles());
            roles.add(Role.ROLE_ADMIN);
            user.setRoles(roles);
        } else {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password is required to create a new admin account.");
            }
            user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));

            Profile profile = new Profile();
            profile.setName(request.getName());
            profile.setEmail(request.getEmail());
            user.setProfile(profile);
        }

        userRepository.save(user);
        return toUserDto(user);
    }

    @Override
    public List<AdminUserDto> getAllAdmins() {
        return userRepository.findByRolesContaining(Role.ROLE_ADMIN).stream()
                .map(this::toUserDto)
                .toList();
    }

    @Override
    public void revokeAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.remove(Role.ROLE_ADMIN);
        if (roles.isEmpty()) {
            roles.add(Role.ROLE_USER);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    private Profile getProfileOrThrow(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
    }

    private AdminUserDto toUserDto(User user) {
        String name = user.getProfile() != null ? user.getProfile().getName() : null;
        return new AdminUserDto(user.getId(), user.getEmail(), name, user.getRoles());
    }

    private AdminProfileSummaryDto toSummaryDto(Profile profile) {
        AdminProfileSummaryDto dto = new AdminProfileSummaryDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setEmail(profile.getEmail());
        dto.setGender(profile.getGender());
        dto.setAge(profile.getAge());
        dto.setCity(profile.getCity());
        dto.setCountry(profile.getCountry());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        return dto;
    }

    private AdminProfileDetailDto toDetailDto(Profile profile) {
        AdminProfileDetailDto dto = new AdminProfileDetailDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setAge(profile.getAge());
        dto.setGender(profile.getGender());
        dto.setBirthday(profile.getBirthday());
        dto.setAddress(profile.getAddress());
        dto.setHeight(profile.getHeight());
        dto.setWeight(profile.getWeight());
        dto.setEmail(profile.getEmail());
        dto.setPhone(profile.getPhone());
        dto.setEducation(profile.getEducation());
        dto.setEthnicity(profile.getEthnicity());
        dto.setMaritalStatus(profile.getMaritalStatus());
        dto.setNationality(profile.getNationality());
        dto.setSecondNationality(profile.getSecondNationality());
        dto.setMotherName(profile.getMotherName());
        dto.setFatherName(profile.getFatherName());
        dto.setFatherOccupation(profile.getFatherOccupation());
        dto.setMotherOccupation(profile.getMotherOccupation());
        dto.setNumberOfSiblings(profile.getNumberOfSiblings());
        dto.setCity(profile.getCity());
        dto.setCountry(profile.getCountry());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());

        userRepository.findByProfileId(profile.getId()).ifPresent(user -> dto.setUserId(user.getId()));

        partnerPreferenceRepository.findByProfileId(profile.getId())
                .map(this::toPreferenceDto)
                .ifPresent(dto::setPartnerPreference);

        return dto;
    }

    private PartnerPreferenceDto toPreferenceDto(PartnerPreference preference) {
        PartnerPreferenceDto dto = modelMapper.map(preference, PartnerPreferenceDto.class);
        dto.setProfileId(preference.getProfile().getId());
        return dto;
    }
}
