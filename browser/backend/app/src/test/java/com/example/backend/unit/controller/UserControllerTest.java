package com.example.backend.unit.controller;

import com.example.backend.entities.User;
import com.example.backend.services.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    private User testUser;
    private String expectedJson;

    @BeforeEach
    public void setup() {
        testUser = new User(1, "test@gmail.com", "password");
        expectedJson = "{\"id\":1,\"email\":\"test@gmail.com\",\"password\":\"password\"}";
    }

    @Test
    public void testGetUserById() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testUser, HttpStatus.OK));

        MvcResult result = mockMvc.perform(get("/api/users/1")).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserByIdFail() throws Exception {
        when(service.getById(13L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get("/api/users/13")).andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    public void testAddUser() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(User.class))).thenReturn(new ResponseEntity<>(testUser, HttpStatus.CREATED));

        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateUser() throws Exception {
        testUser.setEmail("again@gmail.com");
        testUser.setPassword("qwerty");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(User.class))).thenReturn(new ResponseEntity<>(testUser, HttpStatus.OK));

        MvcResult result = mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"again@gmail.com\",\"password\":\"qwerty\"}"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String expectedAfterUpdate = "{\"id\":1,\"email\":\"again@gmail.com\",\"password\":\"qwerty\"}";
        assertEquals(expectedAfterUpdate, result.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(testUser, HttpStatus.OK));

        MvcResult result = mockMvc.perform(delete("/api/users/1")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

}
