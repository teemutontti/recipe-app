package com.example.backend.unit.repository;

import com.example.backend.entities.Food;
import com.example.backend.entities.User;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FoodRepositoryTest {

    @Autowired
    private FoodRepository repository;

    private Food testFood;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        repository.deleteAll();
    }

    @Test
    public void testFindById() {
        Food saved = repository.save(testFood);

        Long savedId = Long.valueOf(saved.getId());
        Optional<Food> found = repository.findById(savedId);

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    public void testSaveUser() {
        Food saved = repository.save(testFood);

        assertNotNull(saved.getId());
        assertEquals("Kanan rintafilee", saved.getName());
        assertEquals(240.0, saved.getCalories());
    }

    @Test
    public void testUpdateUser() {
        Food saved = repository.save(testFood);

        saved.setName("Kalkkunan rintafilee");
        saved.setCalories(124.0);
        Food updated = repository.save(saved);

        assertEquals("Kalkkunan rintafilee", updated.getName());
        assertEquals(124.0, updated.getCalories());
    }

    @Test
    public void testDelete() {
        Food saved = repository.save(testFood);

        repository.delete(saved);
        Long savedId = Long.valueOf(saved.getId());
        Optional<Food> found = repository.findById(savedId);

        assertFalse(found.isPresent());
    }
}
