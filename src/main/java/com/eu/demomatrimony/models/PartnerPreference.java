package com.eu.demomatrimony.models;

import jakarta.persistence.*;

@Entity
@Table(name = "partner_preferences")
public class PartnerPreference extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private Profile profile;

    private Long minAge;
    private Long maxAge;
    private Double minHeight;
    private Double maxHeight;
    private String preferredGender;
    private String preferredCity;
    private String preferredCountry;
    private String preferredEducation;
    private String preferredMaritalStatus;

    public PartnerPreference() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Long getMinAge() {
        return minAge;
    }

    public void setMinAge(Long minAge) {
        this.minAge = minAge;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public Double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Double minHeight) {
        this.minHeight = minHeight;
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getPreferredCity() {
        return preferredCity;
    }

    public void setPreferredCity(String preferredCity) {
        this.preferredCity = preferredCity;
    }

    public String getPreferredCountry() {
        return preferredCountry;
    }

    public void setPreferredCountry(String preferredCountry) {
        this.preferredCountry = preferredCountry;
    }

    public String getPreferredEducation() {
        return preferredEducation;
    }

    public void setPreferredEducation(String preferredEducation) {
        this.preferredEducation = preferredEducation;
    }

    public String getPreferredMaritalStatus() {
        return preferredMaritalStatus;
    }

    public void setPreferredMaritalStatus(String preferredMaritalStatus) {
        this.preferredMaritalStatus = preferredMaritalStatus;
    }
}