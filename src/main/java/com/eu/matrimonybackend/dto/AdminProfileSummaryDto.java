package com.eu.matrimonybackend.dto;

public class AdminProfileSummaryDto {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private Long age;
    private String city;
    private String country;
    private String profilePictureUrl;

    public AdminProfileSummaryDto() {}

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Long getAge() { return age; }
    public void setAge(Long age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
