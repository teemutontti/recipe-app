package com.example.backend.unit.repository;

import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.sql.Time;
import java.time.LocalDate;
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
        testLog = new Log(1, LocalDate.of(2025, 1, 15), Time.valueOf("09:00:00"), "BREAKFAST", 1, 1, 22);
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
        Log log1 = new Log(1, LocalDate.of(2025, 1, 15), Time.valueOf("09:00:00"), "BREAKFAST", 1, 1, 22);
        Log log2 = new Log(2, LocalDate.of(2025, 1, 15), Time.valueOf("13:00:00"), "LUNCH", 1, 2, 150);

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

        saved.setAmount(120);
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
        Log log1 = new Log(1, LocalDate.of(2025, 1, 15), Time.valueOf("09:00:00"), "BREAKFAST", 1, 1, 22);
        Log log2 = new Log(2, LocalDate.of(2025, 1, 15), Time.valueOf("13:00:00"), "LUNCH", 1, 2, 150);
        Log log3 = new Log(3, LocalDate.of(2025, 1, 20), Time.valueOf("13:00:00"), "LUNCH", 1, 2, 150);

        repository.save(log1);
        repository.save(log2);
        repository.save(log3);

        List<Log> found1 = repository.findByDate(LocalDate.of(2025, 1, 15));
        assertEquals(2, found1.size());

        List<Log> found2 = repository.findByDate(LocalDate.of(2025, 1, 20));
        assertEquals(1, found2.size());
    }
}
