package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.admin.AdminUserController;
import com.example.backend.dto.UserDto;
import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto testUser;
    private final String baseUrl = "/api/admin/users";

    @BeforeEach
    public void setup() {
        testUser = new UserDto(1,"test@gmail.com", "pA55word!", Role.ROLE_USER);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnLogs() throws Exception {
        User user1 = new User(1, "maija@gmail.com", "password", Role.ROLE_USER);
        User user2 = new User(2, "essi@gmail.com", "qwerty", Role.ROLE_USER);

        List<User> users = List.of(user1, user2);

        Pageable pageable = PageRequest.of(1, 10);
        Page<User> mockPage = new PageImpl<>(users, pageable, users.size());

        // Mock service layer
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].email").value("maija@gmail.com"))
                .andExpect(jsonPath("$.content[1].email").value("essi@gmail.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetById_AsAdmin_ReturnOk() throws Exception {
        when(service.getById(1L))
                .thenReturn(new ResponseEntity<>(testUser.toUser(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.password", CoreMatchers.is("pA55word!")));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetById_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetById_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser_AsAdmin_ReturnUser() throws Exception {
        testUser.setEmail("again@gmail.com");
        testUser.setPassword("mYS3cur3!Pa55");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(UserDto.class)))
                .thenReturn(new ResponseEntity<>(testUser.toUser(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("again@gmail.com")))
                .andExpect(jsonPath("$.password", CoreMatchers.is("mYS3cur3!Pa55")));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ValueSource(strings = { "email", "password" })
    public void testUpdateUser_AsAdmin_InvalidInput_ReturnBadRequest(String values) throws Exception {
        switch (values) {
            case "email":
                testUser.setEmail("mywebsite.fi");
            case "password":
                testUser.setPassword("qwerty");
        }

        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateUser_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl)).andExpect(status().isForbidden());

    }

    @Test
    public void testUpdateUser_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl)).andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser_AsAdmin_ReturnEmpty() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete(baseUrl + "/{id}", 1)).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }
}
