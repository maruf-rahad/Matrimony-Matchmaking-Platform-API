package com.eu.matrimonybackend.dto;

public class AdminStatsDto {
    private long totalProfiles;
    private long totalAdmins;

    public AdminStatsDto() {}

    public AdminStatsDto(long totalProfiles, long totalAdmins) {
        this.totalProfiles = totalProfiles;
        this.totalAdmins = totalAdmins;
    }

    public long getTotalProfiles() { return totalProfiles; }
    public void setTotalProfiles(long totalProfiles) { this.totalProfiles = totalProfiles; }

    public long getTotalAdmins() { return totalAdmins; }
    public void setTotalAdmins(long totalAdmins) { this.totalAdmins = totalAdmins; }
}
