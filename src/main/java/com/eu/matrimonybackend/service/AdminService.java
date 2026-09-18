package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.AdminProfileDetailDto;
import com.eu.matrimonybackend.dto.AdminProfileSummaryDto;
import com.eu.matrimonybackend.dto.AdminStatsDto;
import com.eu.matrimonybackend.dto.AdminUserDto;
import com.eu.matrimonybackend.dto.CreateAdminRequestDto;

import java.util.List;

public interface AdminService {
    AdminStatsDto getStats();

    List<AdminProfileSummaryDto> getAllProfiles();

    AdminProfileDetailDto getProfileById(Long id);

    void deleteProfile(Long id);

    AdminUserDto createAdmin(CreateAdminRequestDto request);

    List<AdminUserDto> getAllAdmins();

    void revokeAdminRole(Long userId);
}
