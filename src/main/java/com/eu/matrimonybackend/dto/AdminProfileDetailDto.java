package com.eu.matrimonybackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminProfileDetailDto {
    private Long id;
    private Long userId;
    private String name;
    private Long age;
    private String gender;
    private LocalDate birthday;
    private String address;
    private Double height;
    private Double weight;
    private String email;
    private String phone;
    private String education;
    private String ethnicity;
    private String maritalStatus;
    private String nationality;
    private String secondNationality;
    private String motherName;
    private String fatherName;
    private String fatherOccupation;
    private String motherOccupation;
    private String numberOfSiblings;
    private String city;
    private String country;
    private String profilePictureUrl;
    private PartnerPreferenceDto partnerPreference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getAge() { return age; }
    public void setAge(Long age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getSecondNationality() { return secondNationality; }
    public void setSecondNationality(String secondNationality) { this.secondNationality = secondNationality; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getFatherOccupation() { return fatherOccupation; }
    public void setFatherOccupation(String fatherOccupation) { this.fatherOccupation = fatherOccupation; }

    public String getMotherOccupation() { return motherOccupation; }
    public void setMotherOccupation(String motherOccupation) { this.motherOccupation = motherOccupation; }

    public String getNumberOfSiblings() { return numberOfSiblings; }
    public void setNumberOfSiblings(String numberOfSiblings) { this.numberOfSiblings = numberOfSiblings; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public PartnerPreferenceDto getPartnerPreference() { return partnerPreference; }
    public void setPartnerPreference(PartnerPreferenceDto partnerPreference) { this.partnerPreference = partnerPreference; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
