package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.FoodController;
import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Food testFood;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "2025-01-01", "2025-01-01");
    }

    // ====================
    // CREATE Food Tests
    // ====================
    @Test
    void testCreateFood_ReturnCreated() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.CREATED));

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(testFood.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(testFood.getCalories())));
    }

    @ParameterizedTest
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

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());
    }


    // ====================
    // READ Food Tests
    // ====================
    @Test
    void testGetById_ReturnOk() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        mockMvc.perform(get("/api/foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(testFood.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(testFood.getCalories())));
    }

    @Test
    void testGetById_ReturnNotFound() throws Exception {
        when(service.getById(13L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/api/foods/{id}", 13)).andExpect(status().isNotFound());
    }

    // ====================
    // UPDATE Food Tests
    // ====================
    @Test
    void testUpdateFood_AllFields_ReturnFood() throws Exception {
        testFood.setName("New name");
        testFood.setCalories(150.0);
        testFood.setBarcode("1536473434");
        testFood.setServingSize(400);
        testFood.setCarbs(128.0);
        testFood.setProtein(24.0);
        testFood.setFat(45.0);

        // Use eq(1L) to match the exact ID and any(Food.class) to allow any User instance.
        when(service.getById(eq(1L))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));
        when(service.update(eq(1L), any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        mockMvc.perform(patch("/api/foods/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is("New name")))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(150.0)))
                .andExpect(jsonPath("$.barcode", CoreMatchers.is("1536473434")))
                .andExpect(jsonPath("$.servingSize", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.carbs", CoreMatchers.is(128.0)))
                .andExpect(jsonPath("$.protein", CoreMatchers.is(24.0)))
                .andExpect(jsonPath("$.fat", CoreMatchers.is(45.0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "name", "barcode", "servingSize", "calories", "carbs", "protein", "fat", "editedBy", "edited" })
    void testUpdateFood_EachField_ReturnOk(String missingField) throws Exception {
        switch (missingField) {
            case "name":
                testFood.setName("New name");
                break;
            case "barcode":
                testFood.setCalories(140.0);
                break;
            case "servingSize":
                testFood.setServingSize(140);
                break;
            case "calories":
                testFood.setCalories(320.0);
                break;
            case "carbs":
                testFood.setCarbs(29.0);
                break;
            case "protein":
                testFood.setProtein(52.0);
                break;
            case "fat":
                testFood.setFat(12.0);
                break;
            case "editedBy":
                testFood.setEditedBy(8);
                break;
            case "edited":
                testFood.setEdited("2025-01-23");
                break;
        }

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFood_InvalidObject_ReturnsNotFound() throws Exception {
        testFood.setCalories(-100.0);

        mockMvc.perform(patch("/api/foods/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateFood_InvalidId_ReturnsNotFound() throws Exception {
        when(service.getById(eq(99L))).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch("/api/foods/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isNotFound());
    }

    // ====================
    // DELETE Food Tests
    // ====================
    @Test
    void testDeleteFood_ReturnOk() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete("/api/foods/1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }
}
