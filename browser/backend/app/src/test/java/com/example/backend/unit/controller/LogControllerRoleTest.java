package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.LogController;
import com.example.backend.entities.Log;
import com.example.backend.services.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class LogControllerRoleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogService service;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnLogs() throws Exception {
        Log log1 = new Log(1, LocalDate.of(2025, 1, 15), Time.valueOf("00:00:00"), "BREAKFAST", 1, 1, 24);
        Log log2 = new Log(2, LocalDate.of(2025, 1, 15), Time.valueOf("00:00:00"), "LUNCH", 1, 2, 120);

        List<Log> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(service.getAll()).thenReturn(new ResponseEntity<>(logs, HttpStatus.OK));

        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)))
                .andExpect(jsonPath("$[0].meal", CoreMatchers.is("BREAKFAST")))
                .andExpect(jsonPath("$[1].meal", CoreMatchers.is("LUNCH")));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll_AsUser_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/logs")).andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll_AsNobody_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/logs")).andExpect(status().isForbidden());
    }
}
