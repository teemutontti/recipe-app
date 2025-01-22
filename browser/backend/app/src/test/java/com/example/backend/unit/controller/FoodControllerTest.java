package com.example.backend.unit.controller;

import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
    }

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

    @Test
    void testCreateFoodWithoutCalories_ReturnError() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        testFood.setCalories(null);

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFoodWithoutName_ReturnBadRequest() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        testFood.setName(null);

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testCreateFoodWithoutCreatedBy_ReturnBadRequest() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        testFood.setCreatedBy(null);

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFood)))
                .andExpect(status().isBadRequest());
    }

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

    @Test
    void testGetAll_ReturnsFoods() throws Exception {
        Food food1 = new Food(1, "Kana", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        List<Food> foods = new ArrayList<>();
        foods.add(food1);
        foods.add(food2);

        when(service.getAll()).thenReturn(new ResponseEntity<>(foods, HttpStatus.OK));

        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)))
                .andExpect(jsonPath("$[0].name", CoreMatchers.is("Kana")))
                .andExpect(jsonPath("$[1].name", CoreMatchers.is("Riisi")));
    }

    @Test
    void testUpdateFood_ReturnFood() throws Exception {
        testFood.setName("New name");
        testFood.setCalories(150.0);
        testFood.setBarcode("1536473434");
        testFood.setServingSize(400);
        testFood.setCarbs(128.0);
        testFood.setProtein(24.0);
        testFood.setFat(45.0);

        // Use eq(1L) to match the exact ID and any(Food.class) to allow any User instance.
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

    @Test
    void testDeleteFood_ReturnOk() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete("/api/foods/1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }
}
