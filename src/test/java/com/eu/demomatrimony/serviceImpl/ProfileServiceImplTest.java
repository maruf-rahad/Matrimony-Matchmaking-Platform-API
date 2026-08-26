package com.eu.demomatrimony.serviceImpl;

import com.eu.demomatrimony.exeptions.ResourceNotFoundException;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProfiles() {
        Profile profile = getProfile();
        Profile profile2 = getProfile2();

        when(profileRepository.findAll()).thenReturn(Arrays.asList(profile, profile2));

        List<Profile> profiles = profileService.getAllProfiles();

        assertEquals(2, profiles.size());

        assertEquals(1L, profiles.get(0).getId());
        assertEquals("Maruf", profiles.get(0).getName());
        assertEquals(28L, profiles.get(0).getAge());
        assertEquals("Male", profiles.get(0).getGender());
        assertEquals(profile.getBirthday(), profiles.get(0).getBirthday());

        assertEquals(2L, profiles.get(1).getId());
        assertEquals("Rahad", profiles.get(1).getName());
        assertEquals(30L, profiles.get(1).getAge());
        assertEquals("Male", profiles.get(1).getGender());
        assertEquals(profile2.getBirthday(), profiles.get(1).getBirthday());
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void testGetProfileById_Found() {
        Profile profile = getProfile();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Profile found = profileService.getProfileById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Maruf", found.getName());
        assertEquals(28L, found.getAge());
        assertEquals("Male", found.getGender());
        assertEquals(profile.getBirthday(), found.getBirthday());

        verify(profileRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProfileById_NotFound() {
        when(profileRepository.findById(5L)).thenReturn(Optional.empty());

        ResourceNotFoundException elementException = assertThrows(ResourceNotFoundException.class,
                () -> profileService.getProfileById(5L));
        assertEquals("Profile not found with id: 5", elementException.getMessage());

        verify(profileRepository, times(1)).findById(5L);
    }

    @Test
    void testCreateProfile() {
        Profile profile = getProfile();
        when(profileRepository.save(profile)).thenReturn(profile);

        Profile created = profileService.createProfile(profile);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Maruf", created.getName());
        assertEquals(28L, created.getAge());
        assertEquals("Male", created.getGender());
        assertEquals(profile.getBirthday(), created.getBirthday());
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void testUpdateProfile() {
        Profile profile = getProfile();
        Profile updatedProfile = getProfile2();
        updatedProfile.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(updatedProfile);

        Profile result = profileService.updateProfile(1L, updatedProfile);

        assertNotNull(result);
        assertEquals("Rahad", result.getName());
        assertEquals(30L, result.getAge());
        assertEquals("Male", result.getGender());
        assertEquals(updatedProfile.getBirthday(), result.getBirthday());

        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testDeleteProfileById() {
        Profile profile = getProfile();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        doNothing().when(profileRepository).deleteById(1L);

        profileService.deleteProfileById(1L);

        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).deleteById(1L);
    }

    private Profile getProfile() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setName("Maruf");
        profile.setAge(28L);
        profile.setGender("Male");
        profile.setBirthday(LocalDate.of(1997, 5, 20));
        profile.setEmail("maruf@example.com");

        return profile;
    }

    private Profile getProfile2() {
        Profile profile = new Profile();
        profile.setId(2L);
        profile.setName("Rahad");
        profile.setAge(30L);
        profile.setGender("Male");
        profile.setBirthday(LocalDate.of(1998, 5, 20));
        profile.setEmail("rahad@example.com");

        return profile;
    }
}
