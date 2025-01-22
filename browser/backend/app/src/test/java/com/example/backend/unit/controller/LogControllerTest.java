package com.example.backend.unit.controller;

import com.example.backend.entities.Log;
import com.example.backend.services.LogService;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Log testLog;

    @BeforeEach
    public void setup() {
        testLog = new Log(1, LocalDate.of(2025, 1, 15), Time.valueOf("00:00:00"), "BREAKFAST", 1, 1, 24);
    }

    @Test
    public void testCreateLog_ReturnCreated() throws Exception {
        // Use any(Log.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(Log.class))).thenReturn(new ResponseEntity<>(testLog, HttpStatus.CREATED));

        mockMvc.perform(post("/api/logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLog)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", CoreMatchers.is(testLog.getAmount())))
                .andExpect(jsonPath("$.meal", CoreMatchers.is(testLog.getMeal())));
    }

    @Test
    public void testGetById_ReturnOk() throws Exception {
        when(service.getById(1L)).thenReturn(new ResponseEntity<>(testLog, HttpStatus.OK));

        mockMvc.perform(get("/api/logs/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", CoreMatchers.is(testLog.getAmount())))
                .andExpect(jsonPath("$.meal", CoreMatchers.is(testLog.getMeal())));

        verify(service, times(1)).getById(1L);
    }

    @Test
    public void testGetAll_ReturnLogs() throws Exception {
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
    public void testUpdateLog_ReturnFood() throws Exception {
        testLog.setAmount(100);
        testLog.setMeal("SNACKS");

        // Use eq(1L) to match the exact ID and any(Log.class) to allow any User instance.
        when(service.update(eq(1L), any(Log.class))).thenReturn(new ResponseEntity<>(testLog, HttpStatus.OK));

        mockMvc.perform(patch("/api/logs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", CoreMatchers.is(100)))
                .andExpect(jsonPath("$.meal", CoreMatchers.is("SNACKS")));
    }

    @Test
    public void testDeleteLog_ReturnEmpty() throws Exception {
        when(service.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete("/api/logs/1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void testGetByDate_ReturnLogs() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 15);

        Log log1 = new Log(1, date, Time.valueOf("00:00:00"), "BREAKFAST", 1, 1, 24);
        Log log2 = new Log(2, date, Time.valueOf("00:00:00"), "LUNCH", 1, 2, 120);

        List<Log> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(service.getLogsByDate(date)).thenReturn(new ResponseEntity<>(logs, HttpStatus.OK));

        mockMvc.perform(get("/api/logs/by-date")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }
}
