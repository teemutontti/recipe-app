package com.example.backend.unit.service;

import com.example.backend.config.SecurityConfig;
import com.example.backend.entities.Log;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.repositories.LogRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.LogService;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtTokenUtil;
import com.example.backend.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class LogServiceTest {

    @InjectMocks
    private LogService service;

    @Mock
    private LogRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Log testLog;

    @BeforeEach
    public void setup() {
        testLog = new Log(1, LocalDate.of(2025, 1, 15), LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ReturnsLog() {
        when(repository.save(any(Log.class))).thenReturn(testLog);

        ResponseEntity<Log> response = service.create(testLog);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().getId() > 0);
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMeal());
    }

    @Test
    public void testFindById_ReturnsLog() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(testLog));

        ResponseEntity<Log> response = service.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getId() > 0);
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMeal());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        Log log1 = new Log(1, LocalDate.of(2025, 1, 15), LocalTime.of(0,0, 0), "BREAKFAST", 1, 1, 22.0);
        Log log2 = new Log(2, LocalDate.of(2025, 1, 15), LocalTime.of(0,0, 0), "LUNCH", 1, 2, 120.0);

        List<Log> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(repository.findAll()).thenReturn(logs);

        ResponseEntity<List<Log>> response = service.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        when(repository.findById(1L)).thenReturn(Optional.of(testLog));
        when(repository.save(any(Log.class))).thenReturn(testLog);

        testLog.setMeal("LUNCH");
        testLog.setAmount(54.0);
        ResponseEntity<Log> response = service.update(1L, testLog);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0);
        assertEquals("LUNCH", response.getBody().getMeal());
        assertEquals(54, response.getBody().getAmount());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(1L);

        ResponseEntity<Log> response = service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testFindLogsByDate_ReturnsMultipleLogs() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        Log log1 = new Log(1, date, LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        Log log2 = new Log(2, date, LocalTime.of(9,0, 0), "LUNCH", 1, 2, 120.0);

        List<Log> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(repository.findByDateAndUserId(date, 1)).thenReturn(logs);

        ResponseEntity<List<Log>> response = service.getLogsByDateAndUser(date, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFindLogsByUser_ReturnsMultipleLogs() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        Log log1 = new Log(1, date, LocalTime.of(9,0, 0), "BREAKFAST", 19, 1, 22.0);
        Log log2 = new Log(2, date, LocalTime.of(9,0, 0), "LUNCH", 19, 2, 120.0);

        List<Log> logs = new ArrayList<>();
        logs.add(log1);
        logs.add(log2);

        when(repository.findByUserId(eq(19))).thenReturn(logs);

        ResponseEntity<List<Log>> response = service.getLogsByUserId(19);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
