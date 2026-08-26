package com.eu.demomatrimony.controllers;

import com.eu.demomatrimony.models.Profile;
import com.eu.demomatrimony.repositories.InterestRepository;
import com.eu.demomatrimony.repositories.ProfileRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class InterestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private InterestRepository interestRepository;

    private Profile sender;
    private Profile receiver;

    @BeforeEach
    void setUp() {
        interestRepository.deleteAll();
        profileRepository.deleteAll();

        Profile p1 = new Profile();
        p1.setName("Alice");
        p1.setAge(25L);
        p1.setGender("Female");
        p1.setBirthday(LocalDate.of(1999, 1, 1));
        p1.setEmail("alice@example.com");

        Profile p2 = new Profile();
        p2.setName("Bob");
        p2.setAge(27L);
        p2.setGender("Male");
        p2.setBirthday(LocalDate.of(1997, 1, 1));
        p2.setEmail("bob@example.com");

        List<Profile> saved = profileRepository.saveAll(List.of(p1, p2));
        sender = saved.get(0);
        receiver = saved.get(1);
    }

    @Test
    void testSendInterest_Success() throws Exception {
        mockMvc.perform(post("/interests/send")
                        .param("senderId", sender.getId().toString())
                        .param("receiverId", receiver.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderId").value(sender.getId()))
                .andExpect(jsonPath("$.receiverId").value(receiver.getId()))
                .andExpect(jsonPath("$.status").value("PENDING"));

        assertEquals(1, interestRepository.count());
    }

    @Test
    void testSendInterest_SelfInterest_BadRequest() throws Exception {
        mockMvc.perform(post("/interests/send")
                        .param("senderId", sender.getId().toString())
                        .param("receiverId", sender.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetReceivedInterests() throws Exception {
        // Send initial interest request
        mockMvc.perform(post("/interests/send")
                        .param("senderId", sender.getId().toString())
                        .param("receiverId", receiver.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/interests/received/{receiverId}", receiver.getId())
                        .param("status", "PENDING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].senderId").value(sender.getId()))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}