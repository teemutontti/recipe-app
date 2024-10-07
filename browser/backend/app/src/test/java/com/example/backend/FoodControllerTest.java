package com.example.backend;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final long KNOWN_ID = 3L;

    @Test
    void testGetAll() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/foods"));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void testCreateFoodShouldReturnOk() throws Exception {
        String foodJson = "{\"name\":\"Härän liha\",\"calories\":\"260\",\"createdBy\":\"1\"}";
        ResultActions result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson));

        result.andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void testCreateFoodSWithoutCaloriesShouldReturnError() throws Exception {
        String foodJson = "{\"name\":\"Härän liha\",\"createdBy\":\"1\"}";
        ResultActions result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson));

        result.andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    void testCreateFoodSWithoutNameShouldReturnError() throws Exception {
        String foodJson = "{\"calories\":\"260\",\"createdBy\":\"1\"}";
        ResultActions result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson));

        result.andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    void testCreateFoodSWithoutCreatedByShouldReturnError() throws Exception {
        String foodJson = "{\"name\":\"Härän liha\",\"calories\":\"260\"}";
        ResultActions result = mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson));

        result.andExpect(status().isInternalServerError());
    }

    @Test
    void testGetByIdShouldReturnOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/foods/{id}", KNOWN_ID));
        result.andExpect(status().isOk());
    }

    @Test
    void testGetByIdShouldReturnNotFound() throws Exception {
        long NOT_FOUND_ID = 9386587357L;
        ResultActions result = mockMvc.perform(get("/api/foods/{id}", NOT_FOUND_ID));
        result.andExpect(status().isNotFound());
    }

    @Test
    void testGetByIdShouldReturnBadRequest() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/foods/{id}", 5.5F));
        result.andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testUpdateAllValues() throws Exception {
        String updatedFoodJson = "{" +
                "\"name\":\"Uusi nimi\"," +
                "\"calories\":\"150\"," +
                "\"barcode\":\"1536473434\"," +
                "\"servingSize\":\"400\"," +
                "\"carbs\":\"127.0\"," +
                "\"protein\":\"24.0\"," +
                "\"fat\":\"45.0\"" +
                "}";
        ResultActions result = mockMvc.perform(patch("/api/foods/{id}", KNOWN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Uusi nimi"))
                .andExpect(jsonPath("$.calories").value("150"))
                .andExpect(jsonPath("$.barcode").value("1536473434"))
                .andExpect(jsonPath("$.servingSize").value("400"))
                .andExpect(jsonPath("$.carbs").value("127.0"))
                .andExpect(jsonPath("$.protein").value("24.0"))
                .andExpect(jsonPath("$.fat").value("45.0"));
    }

    @Test
    @Transactional
    void testUpdateFoodName() throws Exception {
        String updatedFoodJson = "{\"name\":\"Uusi nimi\"}";
        ResultActions result = mockMvc.perform(patch("/api/foods/{id}", KNOWN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Uusi nimi"));
    }

    @Test
    @Transactional
    void testUpdateBarcode() throws Exception {
        String updatedFoodJson = "{\"barcode\":\"BGJ%#%hjghfJJET3535j#\"}";
        ResultActions result = mockMvc.perform(patch("/api/foods/{id}", KNOWN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("BGJ%#%hjghfJJET3535j#"));
    }

    @Test
    @Transactional
    void testUpdateEditedBy() throws Exception {
        String updatedFoodJson = "{\"edited\":\"10\"}";
        ResultActions result = mockMvc.perform(patch("/api/foods/{id}", KNOWN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFoodJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.edited").value("10"));
    }

    @Test
    @Transactional
    void testDelete() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/foods/{id}", KNOWN_ID));
        result.andExpect(status().isOk());
    }
}
