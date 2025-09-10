package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ProfileControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private List<Profile>savedProfile;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        Profile profile = getProfile1();
        Profile profile2 = getProfile2();
        savedProfile = profileRepository.saveAll(List.of(profile, profile2));
    }

    @Test
    void testGetAllProfiles() throws Exception {
        mockMvc.perform(get("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].birthday").value("1993-01-01"))
                .andExpect(jsonPath("$[0].address").value("123 Street, City"))
                .andExpect(jsonPath("$[0].height").value(180.5))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].phone").value("1234567890"))
                .andExpect(jsonPath("$[0].education").value("Bachelor"))
                .andExpect(jsonPath("$[0].ethnicity").value("Asian"))
                .andExpect(jsonPath("$[0].maritalStatus").value("Single"))
                .andExpect(jsonPath("$[0].nationality").value("American"))
                .andExpect(jsonPath("$[0].secondNationality").value("Canadian"))
                .andExpect(jsonPath("$[0].motherName").value("Mary Doe"))
                .andExpect(jsonPath("$[0].fatherName").value("Robert Doe"))
                .andExpect(jsonPath("$[0].fatherOccupation").value("Engineer"))
                .andExpect(jsonPath("$[0].motherOccupation").value("Teacher"))
                .andExpect(jsonPath("$[0].numberOfSiblings").value("2"))

                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].age").value(28))
                .andExpect(jsonPath("$[1].gender").value("Female"))
                .andExpect(jsonPath("$[1].birthday").value("1995-02-15"))
                .andExpect(jsonPath("$[1].address").value("456 Avenue, Town"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"))
                .andExpect(jsonPath("$[1].phone").value("9876543210"))
                .andExpect(jsonPath("$[1].education").value("Master"))
                .andExpect(jsonPath("$[1].ethnicity").value("European"))
                .andExpect(jsonPath("$[1].maritalStatus").value("Married"))
                .andExpect(jsonPath("$[1].nationality").value("British"))
                .andExpect(jsonPath("$[1].secondNationality").value("French"))
                .andExpect(jsonPath("$[1].motherName").value("Anna Smith"))
                .andExpect(jsonPath("$[1].fatherName").value("James Smith"))
                .andExpect(jsonPath("$[1].fatherOccupation").value("Doctor"))
                .andExpect(jsonPath("$[1].motherOccupation").value("Nurse"))
                .andExpect(jsonPath("$[1].numberOfSiblings").value("1"));
    }

    @Test
    void testGetProfileById_Success() throws Exception {
        mockMvc.perform(get("/profile/{id}", savedProfile.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"));
    }

    @Test
    void testGetProfileById_NotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(get("/profile/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Profile getProfile1(){
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

    private Profile getProfile2(){
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


}