package com.eu.matrimonybackend.dto;

import com.eu.matrimonybackend.enums.Role;

import java.util.Set;

public class AdminUserDto {
    private Long userId;
    private String email;
    private String name;
    private Set<Role> roles;

    public AdminUserDto() {}

    public AdminUserDto(Long userId, String email, String name, Set<Role> roles) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
