package com.example.backend.integration;

import com.example.backend.entities.User;
import com.example.backend.utils.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        User user = new User(1, "test@gmail.com", "password");

        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.password").value("REDACTED"))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        User response = objectMapper.readValue(responseJson, User.class);

        String decryptedEmail = SecurityUtil.decrypt(response.getEmail());
        assertEquals("test@gmail.com", decryptedEmail);

    }

    @Test
    void testGetUserById() throws Exception {
        String userJson = "{\"email\":\"maija.meikalainen@gmail.com\",\"password\":\"salasana\"}";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        MvcResult result2 = mockMvc.perform(get("/api/users/{id}", 1)).andReturn();
        assertEquals(200, result2.getResponse().getStatus());
    }
}