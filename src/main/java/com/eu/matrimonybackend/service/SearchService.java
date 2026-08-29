package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.models.Profile;

import java.util.List;

public interface SearchService {

    public List<Profile> searchProfiles(
            String name,
            Long minAge,
            Long maxAge,
            String gender,
            String address,
            String education,
            String ethnicity,
            String maritalStatus,
            String nationality,
            String secondNationality,
            String fatherOccupation,
            String motherOccupation,
            String numberOfSiblings,
            String city,
            String country
    );
}
