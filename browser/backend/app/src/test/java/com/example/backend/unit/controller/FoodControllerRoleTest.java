package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.FoodController;
import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class FoodControllerRoleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_AsAdmin_ReturnsFoods() throws Exception {
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
    @WithMockUser(roles = "ADMIN")
    void testGetAll_ReturnsEmptyList() throws Exception {
        when(service.getAll()).thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/foods")).andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/foods")).andExpect(status().isForbidden());
    }
}
