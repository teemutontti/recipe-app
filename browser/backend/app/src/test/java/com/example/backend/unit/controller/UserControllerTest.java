package com.example.backend.unit.controller;

import com.example.backend.entities.Log;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User(1, "test@gmail.com", "password");
    }

    @Test
    public void testCreateUser_ReturnCreated() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(User.class))).thenReturn(new ResponseEntity<>(testUser, HttpStatus.CREATED));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.password", CoreMatchers.is("password")));
    }

    @Test
    public void testGetById_ReturnOk() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testUser, HttpStatus.OK));

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.password", CoreMatchers.is("password")));
    }

    @Test
    public void testGetAll_ReturnLogs() throws Exception {
        User user1 = new User(1, "maija@gmail.com", "password");
        User user2 = new User(2, "essi@gmail.com", "qwerty");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(service.getAll()).thenReturn(new ResponseEntity<>(users, HttpStatus.OK));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)))
                .andExpect(jsonPath("$[0].email", CoreMatchers.is("maija@gmail.com")))
                .andExpect(jsonPath("$[1].email", CoreMatchers.is("essi@gmail.com")));
    }

    @Test
    public void testUpdateUser_ReturnUser() throws Exception {
        testUser.setEmail("again@gmail.com");
        testUser.setPassword("qwerty");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(User.class))).thenReturn(new ResponseEntity<>(testUser, HttpStatus.OK));

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("again@gmail.com")))
                .andExpect(jsonPath("$.password", CoreMatchers.is("qwerty")));
    }

    @Test
    public void testDeleteUser_ReturnEmpty() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete("/api/users/1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

}
