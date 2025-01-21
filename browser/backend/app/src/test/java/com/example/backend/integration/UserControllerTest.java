package com.example.backend;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final int KNOWN_ID = 4;

    @Test
    void testGetAll() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/users"));
        MockHttpServletResponse response = result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    @Transactional
    void testCreateUserShouldReturnOk() throws Exception {
        String userJson = "{\"email\":\"maija.meikalainen@gmail.com\",\"password\":\"salasana\"}";
        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void testCreateUserShouldReturnError() throws Exception {
        // User with this email is already in the database
        String userJson = "{\"email\":\"essi.esimerkki@gmail.com\",\"password\":\"salasana\"}";
        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUserById() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/users/{id}", KNOWN_ID)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }
}