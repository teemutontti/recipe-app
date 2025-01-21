package com.example.backend.unit.controller;

import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
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
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    private Food testFood;
    private String testFoodJson;
    private String expectedJson;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        testFoodJson = "{\"name\":\"Kanan rintafilee\",\"calories\":\"250.0\",\"createdBy\":\"1\"}";
        expectedJson = "{\"id\":1,\"name\":\"Kanan rintafilee\",\"barcode\":\"1234567890\",\"servingSize\":100,\"calories\":250.0,\"carbs\":0.0,\"protein\":0.0,\"fat\":0.0,\"createdBy\":1,\"editedBy\":1,\"created\":\"\",\"edited\":\"\"}";
    }

    @Test
    void testGetAll() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/foods")).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testCreateFoodShouldReturnOk() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.CREATED));

        MvcResult result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testFoodJson))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void testCreateFoodSWithoutCaloriesShouldReturnError() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        String foodJson = "{\"name\":\"Härän liha\",\"createdBy\":\"1\"}";
        MvcResult result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testCreateFoodSWithoutNameShouldReturnError() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        String foodJson = "{\"calories\":\"260\",\"createdBy\":\"1\"}";
        MvcResult result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testCreateFoodSWithoutCreatedByShouldReturnError() throws Exception {
        // Use any(User.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Food.class))).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        String foodJson = "{\"name\":\"Härän liha\",\"calories\":\"260\"}";
        MvcResult result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testGetByIdShouldReturnOk() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        MvcResult result = mockMvc.perform(get("/api/foods/1")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }

    @Test
    void testGetByIdShouldReturnNotFound() throws Exception {
        when(service.getById(13L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get("/api/foods/{id}", 13)).andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void testGetByIdShouldReturnBadRequest() throws Exception {
        when(service.getById(13L)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        MvcResult result = mockMvc.perform(get("/api/foods/{id}", 13.0)).andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testUpdateAllValues() throws Exception {
        testFood.setName("New name");
        testFood.setCalories(150.0);
        testFood.setBarcode("1536473434");
        testFood.setServingSize(400);
        testFood.setCarbs(128.0);
        testFood.setProtein(24.0);
        testFood.setFat(45.0);

        String updatedFoodJson = "{\"name\":\"Uusi nimi\",\"calories\":\"150\",\"barcode\":\"1536473434\","
                + "\"servingSize\":\"400\",\"carbs\":\"128.0\",\"protein\":\"24.0\",\"fat\":\"45.0\"}";

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        MvcResult result = mockMvc.perform(patch("/api/foods/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String expectedAfterUpdate = "{\"id\":1,\"name\":\"New name\",\"barcode\":\"1536473434\","
                + "\"servingSize\":400,\"calories\":150.0,\"carbs\":128.0,\"protein\":24.0,\"fat\":45.0,\"createdBy\":1,"
                + "\"editedBy\":1,\"created\":\"\",\"edited\":\"\"}";
        assertEquals(expectedAfterUpdate, result.getResponse().getContentAsString());
    }

    @Test
    void testUpdateFoodName() throws Exception {
        testFood.setName("New name again");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        String updatedFoodJson = "{\"name\":\"New name again\"}";
        MvcResult result = mockMvc.perform(patch("/api/foods/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String expectedAfterUpdate = expectedJson.replace("\"name\":\"Kanan rintafilee\"", "\"name\":\"New name again\"");
        assertEquals(expectedAfterUpdate, result.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBarcode() throws Exception {
        testFood.setBarcode("BGJ%#%hjghfJJET3535j#");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        String updatedFoodJson = "{\"barcode\":\"BGJ%#%hjghfJJET3535j#\"}";
        MvcResult result = mockMvc.perform(patch("/api/foods/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String expectedAfterUpdate = expectedJson.replace("\"barcode\":\"1234567890\"", "\"barcode\":\"BGJ%#%hjghfJJET3535j#\"");
        assertEquals(expectedAfterUpdate, result.getResponse().getContentAsString());
    }

    @Test
    void testUpdateEditedBy() throws Exception {
        testFood.setEditedBy(10);

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(1L), any(Food.class))).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        String updatedFoodJson = "{\"editedBy\":\"10\"}";
        MvcResult result = mockMvc.perform(patch("/api/foods/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String expectedAfterUpdate = expectedJson.replace("\"editedBy\":1", "\"editedBy\":10");
        assertEquals(expectedAfterUpdate, result.getResponse().getContentAsString());
    }

    @Test
    void testDelete() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(testFood, HttpStatus.OK));

        MvcResult result = mockMvc.perform(delete("/api/foods/1")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(expectedJson, result.getResponse().getContentAsString());
    }
}
