package com.example.backend.unit.repository;

import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LogRepositoryTest {

    @Autowired
    private LogRepository repository;

    private Log testLog;

    @BeforeEach
    public void setup() {
        testLog = new Log(1, LocalDate.of(2025, 1, 15), LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ReturnsSavedLog() {
        Log saved = repository.save(testLog);

        assertNotNull(saved.getId());
        assertEquals(22, saved.getAmount());
        assertEquals("BREAKFAST", saved.getMeal());
    }

    @Test
    public void testFindById_ReturnsLog() {
        Log saved = repository.save(testLog);

        Log found = repository.findById(Long.valueOf(saved.getId())).get();

        assertNotNull(found);
        assertTrue(found.getId() > 0);
        assertEquals(22, found.getAmount());
        assertEquals("BREAKFAST", found.getMeal());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        Log log1 = new Log(1, LocalDate.of(2025, 1, 15), LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        Log log2 = new Log(2, LocalDate.of(2025, 1, 15), LocalTime.of(13,0, 0), "LUNCH", 1, 2, 150.0);

        repository.save(log1);
        repository.save(log2);

        List<Log> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("BREAKFAST", found.get(0).getMeal());
        assertEquals("LUNCH", found.get(1).getMeal());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        Log saved = repository.save(testLog);

        saved.setAmount(120.0);
        saved.setMeal("SNACKS");
        Log updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(120, updated.getAmount());
        assertEquals("SNACKS", updated.getMeal());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        Log saved = repository.save(testLog);

        repository.delete(saved);
        Optional<Log> found = repository.findById(Long.valueOf(saved.getId()));

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByDate_ReturnsMultipleLogs() {
        Log log1 = new Log(null, LocalDate.of(2025, 1, 15), LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        Log log2 = new Log(null, LocalDate.of(2025, 1, 15), LocalTime.of(13,0, 0), "LUNCH", 1, 2, 150.0);
        Log log3 = new Log(null, LocalDate.of(2025, 1, 20), LocalTime.of(13,0, 0), "LUNCH", 1, 2, 150.0);

        repository.save(log1);
        repository.save(log2);
        repository.save(log3);

        LocalDate date1 = LocalDate.of(2025, 1, 15);
        List<Log> found1 = repository.findByDateAndUserId(date1, 1);
        assertEquals(2, found1.size());

        LocalDate date2 = LocalDate.of(2025, 1, 20);
        List<Log> found2 = repository.findByDateAndUserId(date2, 1);
        assertEquals(1, found2.size());
    }

    @Test
    public void testFindByUserId_ReturnsMultipleLogs() {
        Log log1 = new Log(null, LocalDate.of(2025, 1, 15), LocalTime.of(9,0, 0), "BREAKFAST", 1, 1, 22.0);
        Log log2 = new Log(null, LocalDate.of(2025, 1, 15), LocalTime.of(13,0, 0), "LUNCH", 2, 2, 150.0);
        Log log3 = new Log(null, LocalDate.of(2025, 1, 20), LocalTime.of(13,0, 0), "LUNCH", 1, 2, 150.0);

        repository.save(log1);
        repository.save(log2);
        repository.save(log3);

        List<Log> found1 = repository.findByUserId(1);
        assertEquals(2, found1.size());

        List<Log> found2 = repository.findByUserId(2);
        assertEquals(1, found2.size());
    }
}
