package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.dto.PartnerPreferenceDto;
import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.PartnerPreferenceRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PartnerPreferenceRepository preferenceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Profile user;
    private Profile candidate;

    @BeforeEach
    void setUp() {
        preferenceRepository.deleteAll();
        profileRepository.deleteAll();

        Profile p1 = new Profile();
        p1.setName("John");
        p1.setAge(30L);
        p1.setGender("Male");
        p1.setBirthday(LocalDate.of(1994, 1, 1));
        p1.setEmail("john@example.com");

        Profile p2 = new Profile();
        p2.setName("Jane");
        p2.setAge(26L);
        p2.setGender("Female");
        p2.setHeight(165.0);
        p2.setCity("Stockholm");
        p2.setEducation("Master");
        p2.setMaritalStatus("Single");
        p2.setBirthday(LocalDate.of(1998, 2, 2));
        p2.setEmail("jane@example.com");

        List<Profile> saved = profileRepository.saveAll(List.of(p1, p2));
        user = saved.get(0);
        candidate = saved.get(1);
    }

    @Test
    void testSaveAndGetPreferences_Success() throws Exception {
        PartnerPreferenceDto dto = new PartnerPreferenceDto();
        dto.setPreferredGender("Female");
        dto.setMinAge(22L);
        dto.setMaxAge(28L);
        dto.setPreferredCity("Stockholm");

        // 1. Save preferences
        mockMvc.perform(post("/matches/preferences/{profileId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferredGender").value("Female"))
                .andExpect(jsonPath("$.preferredCity").value("Stockholm"));

        // 2. Fetch preferences
        mockMvc.perform(get("/matches/preferences/{profileId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minAge").value(22))
                .andExpect(jsonPath("$.maxAge").value(28));
    }

    @Test
    void testGetMatches_Success() throws Exception {
        // Save preference targeting the candidate profile
        PartnerPreferenceDto dto = new PartnerPreferenceDto();
        dto.setPreferredGender("Female");
        dto.setMinAge(22L);
        dto.setMaxAge(28L);
        dto.setPreferredCity("Stockholm");
        dto.setPreferredEducation("Master");
        dto.setPreferredMaritalStatus("Single");

        mockMvc.perform(post("/matches/preferences/{profileId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Get Top Matches feed
        mockMvc.perform(get("/matches/{profileId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].candidateProfile.name").value("Jane"))
                .andExpect(jsonPath("$[0].matchPercentage").value(100.0));
    }

    @Test
    void testGetMatches_PreferencesNotFound_Returns404() throws Exception {
        mockMvc.perform(get("/matches/{profileId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}