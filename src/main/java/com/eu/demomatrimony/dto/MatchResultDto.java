package com.eu.demomatrimony.dto;

public class MatchResultDto {
    private ProfileDto candidateProfile;
    private double matchPercentage;

    public MatchResultDto(ProfileDto candidateProfile, double matchPercentage) {
        this.candidateProfile = candidateProfile;
        this.matchPercentage = matchPercentage;
    }

    public ProfileDto getCandidateProfile() { return candidateProfile; }
    public void setCandidateProfile(ProfileDto candidateProfile) { this.candidateProfile = candidateProfile; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }
}