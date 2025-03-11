package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.FoodController;
import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
import com.example.backend.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Food testFood;
    private final String baseUrl = "/api/foods";

    @BeforeEach
    public void setup() throws JOSEException {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "2025-01-01", "2025-01-01");
    }

    @Test
    @WithMockUser
    void testCreateFood_ReturnCreated() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(testFood.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(testFood.getCalories())));
    }

    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "name", "calories", "createdBy", "servingSize" })
    void testCreateFood_InvalidFields_ReturnBadRequest(String missingField) throws Exception {
        switch (missingField) {
            case "name":
                testFood.setName(null);
                break;
            case "calories":
                testFood.setCalories(null);
                break;
            case "createdBy":
                testFood.setCreatedBy(null);
                break;
            case "servingSize":
                testFood.setServingSize(0);
                break;
        }

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFood_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(post(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetById_ReturnOk() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(testFood.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(testFood.getCalories())));
    }

    @Test
    @WithMockUser
    void testGetById_ReturnNotFound() throws Exception {
        when(service.getById(13L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get(baseUrl + "/{id}", 13)).andExpect(status().isNotFound());
    }

    @Test
    void testGetById_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 13)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testQuery_ReturnOk() throws Exception {
        Food food1 = new Food(1, "Kana", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        List<Food> foods = List.of(food1, food2);

        when(service.getFoodsByQuery(anyString())).thenReturn(new ResponseEntity<>(foods, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/query")
                .param("query", "ka"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kana"))
                .andExpect(jsonPath("$[0].calories").value(250.0));
    }
}
