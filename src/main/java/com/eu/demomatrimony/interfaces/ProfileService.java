package com.eu.demomatrimony.interfaces;


import com.eu.demomatrimony.models.Profile;

import java.util.List;

public interface ProfileService {

    public List<Profile> getAllProfiles();

    public Profile getProfileById(Long id);

    public Profile createProfile(Profile profile);

    public Profile updateProfile(Long id, Profile profile);

    public void deleteProfileById(Long id);
}
