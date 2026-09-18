package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.AdminProfileDetailDto;
import com.eu.matrimonybackend.dto.AdminProfileSummaryDto;
import com.eu.matrimonybackend.dto.AdminStatsDto;
import com.eu.matrimonybackend.dto.AdminUserDto;
import com.eu.matrimonybackend.dto.CreateAdminRequestDto;
import com.eu.matrimonybackend.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin APIs", description = "Platform administration endpoints")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<AdminProfileSummaryDto>> getAllProfiles() {
        return ResponseEntity.ok(adminService.getAllProfiles());
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<AdminProfileDetailDto> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProfileById(id));
    }

    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        adminService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminUserDto>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PostMapping("/create-admin")
    public ResponseEntity<AdminUserDto> createAdmin(@RequestBody CreateAdminRequestDto request) {
        return ResponseEntity.ok(adminService.createAdmin(request));
    }

    @DeleteMapping("/users/{id}/admin-role")
    public ResponseEntity<Void> revokeAdminRole(@PathVariable Long id) {
        adminService.revokeAdminRole(id);
        return ResponseEntity.noContent().build();
    }
}
