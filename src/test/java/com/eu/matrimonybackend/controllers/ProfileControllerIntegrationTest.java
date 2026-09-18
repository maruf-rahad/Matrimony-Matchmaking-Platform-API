package com.eu.matrimonybackend.controllers;

import com.eu.matrimonybackend.dto.PartnerPreferenceDto;
import com.eu.matrimonybackend.dto.ProfileDto;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.repositories.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@WithMockUser
class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private List<Profile> savedProfile;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        Profile profile = getProfile1();
        Profile profile2 = getProfile2();
        savedProfile = profileRepository.saveAll(List.of(profile, profile2));
    }

    @Test
    void testGetAllProfiles() throws Exception {
        // @WithMockUser has no matching Profile and no ACCEPTED interest with either
        // saved profile, so private fields (phone, email, birthday, parents' info) must
        // be masked while the rest of the biodata stays visible.
        mockMvc.perform(get("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].birthday").value(nullValue()))
                .andExpect(jsonPath("$[0].address").value("123 Street, City"))
                .andExpect(jsonPath("$[0].height").value(180.5))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value(nullValue()))
                .andExpect(jsonPath("$[0].phone").value(nullValue()))
                .andExpect(jsonPath("$[0].education").value("Bachelor"))
                .andExpect(jsonPath("$[0].ethnicity").value("Asian"))
                .andExpect(jsonPath("$[0].maritalStatus").value("Single"))
                .andExpect(jsonPath("$[0].nationality").value("American"))
                .andExpect(jsonPath("$[0].secondNationality").value("Canadian"))
                .andExpect(jsonPath("$[0].motherName").value(nullValue()))
                .andExpect(jsonPath("$[0].fatherName").value(nullValue()))
                .andExpect(jsonPath("$[0].fatherOccupation").value(nullValue()))
                .andExpect(jsonPath("$[0].motherOccupation").value(nullValue()))
                .andExpect(jsonPath("$[0].numberOfSiblings").value("2"))

                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].age").value(28))
                .andExpect(jsonPath("$[1].gender").value("Female"))
                .andExpect(jsonPath("$[1].birthday").value(nullValue()))
                .andExpect(jsonPath("$[1].address").value("456 Avenue, Town"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value(nullValue()))
                .andExpect(jsonPath("$[1].phone").value(nullValue()))
                .andExpect(jsonPath("$[1].education").value("Master"))
                .andExpect(jsonPath("$[1].ethnicity").value("European"))
                .andExpect(jsonPath("$[1].maritalStatus").value("Married"))
                .andExpect(jsonPath("$[1].nationality").value("British"))
                .andExpect(jsonPath("$[1].secondNationality").value("French"))
                .andExpect(jsonPath("$[1].motherName").value(nullValue()))
                .andExpect(jsonPath("$[1].fatherName").value(nullValue()))
                .andExpect(jsonPath("$[1].fatherOccupation").value(nullValue()))
                .andExpect(jsonPath("$[1].motherOccupation").value(nullValue()))
                .andExpect(jsonPath("$[1].numberOfSiblings").value("1"));
    }

    @Test
    void testGetProfileById_Success() throws Exception {
        // Same anonymous-viewer masking as testGetAllProfiles applies here.
        mockMvc.perform(get("/profile/{id}", savedProfile.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.email").value(nullValue()))
                .andExpect(jsonPath("$.phone").value(nullValue()));
    }

    @Test
    void testGetProfileById_NotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(get("/profile/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddProfile_FullDto_Success() throws Exception {
        ProfileDto profileDto = getProfileDto();

        String profileJson = objectMapper.writeValueAsString(profileDto);

        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.birthday").value("1995-05-20"))
                .andExpect(jsonPath("$.address").value("123 Main Street, Stockholm"))
                .andExpect(jsonPath("$.height").value(165.0))
                .andExpect(jsonPath("$.weight").value(60.0))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"))
                .andExpect(jsonPath("$.education").value("Master's in Computer Science"))
                .andExpect(jsonPath("$.ethnicity").value("Caucasian"))
                .andExpect(jsonPath("$.maritalStatus").value("Single"))
                .andExpect(jsonPath("$.nationality").value("Swedish"))
                .andExpect(jsonPath("$.secondNationality").value("Finnish"))
                .andExpect(jsonPath("$.motherName").value("Mary Doe"))
                .andExpect(jsonPath("$.fatherName").value("John Doe Sr."))
                .andExpect(jsonPath("$.motherOccupation").value("Teacher"))
                .andExpect(jsonPath("$.fatherOccupation").value("Engineer"))
                .andExpect(jsonPath("$.numberOfSiblings").value("1"));
    }

    @Test
    void testUpdateProfile_Success() throws Exception {

        ProfileDto updatedDto = getProfileDto();

        String updatedJson = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put("/profile/{id}", savedProfile.get(1).getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedDto.getName()))
                .andExpect(jsonPath("$.age").value(updatedDto.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedDto.getGender()))
                .andExpect(jsonPath("$.birthday").value(updatedDto.getBirthday()))
                .andExpect(jsonPath("$.address").value(updatedDto.getAddress()))
                .andExpect(jsonPath("$.height").value(updatedDto.getHeight()))
                .andExpect(jsonPath("$.weight").value(updatedDto.getWeight()))
                .andExpect(jsonPath("$.email").value(updatedDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(updatedDto.getPhone()))
                .andExpect(jsonPath("$.education").value(updatedDto.getEducation()))
                .andExpect(jsonPath("$.ethnicity").value(updatedDto.getEthnicity()))
                .andExpect(jsonPath("$.maritalStatus").value(updatedDto.getMaritalStatus()))
                .andExpect(jsonPath("$.nationality").value(updatedDto.getNationality()))
                .andExpect(jsonPath("$.secondNationality").value(updatedDto.getSecondNationality()))
                .andExpect(jsonPath("$.motherName").value(updatedDto.getMotherName()))
                .andExpect(jsonPath("$.fatherName").value(updatedDto.getFatherName()))
                .andExpect(jsonPath("$.fatherOccupation").value(updatedDto.getFatherOccupation()))
                .andExpect(jsonPath("$.motherOccupation").value(updatedDto.getMotherOccupation()))
                .andExpect(jsonPath("$.numberOfSiblings").value(updatedDto.getNumberOfSiblings()));
    }

    @Test
    void testDeleteProfile_Success() throws Exception {
        mockMvc.perform(delete("/profile/{id}", savedProfile.get(0).getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(profileRepository.findById(savedProfile.get(0).getId()).isPresent());
    }

    @Test
    void testDeleteProfile_NotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(delete("/profile/{id}", invalidId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProfiles_ByQuery_Success() throws Exception {
        PartnerPreferenceDto criteria = new PartnerPreferenceDto();
        criteria.setPreferredGender("Male"); // Filter specifically for John Doe

        String jsonBody = objectMapper.writeValueAsString(criteria);

        mockMvc.perform(post("/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[0].email").value(nullValue()));
    }

    @Test
    void testSearchProfiles_NoMatch() throws Exception {
        PartnerPreferenceDto criteria = new PartnerPreferenceDto();
        criteria.setPreferredGender("NonExistentGender"); // Filter that matches zero profiles

        String jsonBody = objectMapper.writeValueAsString(criteria);

        mockMvc.perform(post("/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    private Profile getProfile1() {
        Profile profile1 = new Profile();
        profile1.setName("John Doe");
        profile1.setAge(30L);
        profile1.setGender("Male");
        profile1.setBirthday(LocalDate.of(1993, 1, 1));
        profile1.setAddress("123 Street, City");
        profile1.setHeight(180.5);
        profile1.setWeight(75.0);
        profile1.setEmail("john@example.com");
        profile1.setPhone("1234567890");
        profile1.setEducation("Bachelor");
        profile1.setEthnicity("Asian");
        profile1.setMaritalStatus("Single");
        profile1.setNationality("American");
        profile1.setSecondNationality("Canadian");
        profile1.setMotherName("Mary Doe");
        profile1.setFatherName("Robert Doe");
        profile1.setFatherOccupation("Engineer");
        profile1.setMotherOccupation("Teacher");
        profile1.setNumberOfSiblings("2");

        return profile1;
    }

    private Profile getProfile2() {
        Profile profile2 = new Profile();
        profile2.setName("Jane Smith");
        profile2.setAge(28L);
        profile2.setGender("Female");
        profile2.setBirthday(LocalDate.of(1995, 2, 15));
        profile2.setAddress("456 Avenue, Town");
        profile2.setHeight(165.0);
        profile2.setWeight(60.0);
        profile2.setEmail("jane@example.com");
        profile2.setPhone("9876543210");
        profile2.setEducation("Master");
        profile2.setEthnicity("European");
        profile2.setMaritalStatus("Married");
        profile2.setNationality("British");
        profile2.setSecondNationality("French");
        profile2.setMotherName("Anna Smith");
        profile2.setFatherName("James Smith");
        profile2.setFatherOccupation("Doctor");
        profile2.setMotherOccupation("Nurse");
        profile2.setNumberOfSiblings("1");

        return profile2;
    }

    ProfileDto getProfileDto() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setName("Jane Doe");
        profileDto.setAge(28L);
        profileDto.setGender("Female");
        profileDto.setBirthday("1995-05-20");
        profileDto.setAddress("123 Main Street, Stockholm");
        profileDto.setHeight(165.0);
        profileDto.setWeight(60.0);
        profileDto.setEmail("jane@example.com");
        profileDto.setPhone("9876543210");
        profileDto.setEducation("Master's in Computer Science");
        profileDto.setEthnicity("Caucasian");
        profileDto.setMaritalStatus("Single");
        profileDto.setNationality("Swedish");
        profileDto.setSecondNationality("Finnish");
        profileDto.setMotherName("Mary Doe");
        profileDto.setFatherName("John Doe Sr.");
        profileDto.setMotherOccupation("Teacher");
        profileDto.setFatherOccupation("Engineer");
        profileDto.setNumberOfSiblings("1");

        return profileDto;
    }
}