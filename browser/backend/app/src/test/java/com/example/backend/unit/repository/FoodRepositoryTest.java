package com.example.backend.unit.repository;

import com.example.backend.entities.Food;
import com.example.backend.entities.User;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
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
        testFood = new Food(1, "Kanan rintafilee", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        repository.deleteAll();
    }

    @Test
    public void testSaveFood_ReturnsSavedFood() {
        Food saved = repository.save(testFood);

        assertNotNull(saved.getId());
        assertEquals("Kanan rintafilee", saved.getName());
        assertEquals(250.0, saved.getCalories());
    }

    @Test
    public void testFindById_ReturnsFood() {
        Food saved = repository.save(testFood);

        Food found = repository.findById(Long.valueOf(saved.getId())).get();

        assertNotNull(found);
        assertTrue(found.getId() > 0);
        assertEquals("Kanan rintafilee", found.getName());
        assertEquals(250.0, found.getCalories());
    }

    @Test
    public void testFindAll_ReturnsMultipleFoods() {
        Food food1 = new Food(1, "Kana", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 285.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        repository.save(food1);
        repository.save(food2);

        List<Food> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("Kana", found.get(0).getName());
        assertEquals("Riisi", found.get(1).getName());
    }

    @Test
    public void testUpdateFood_ReturnsFood() {
        Food saved = repository.save(testFood);

        saved.setName("Laktoositon maito");
        saved.setCalories(35.0);
        Food updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Laktoositon maito", updated.getName());
        assertEquals(35.0, updated.getCalories());
    }

    @Test
    public void testDeleteFood_ReturnsEmptyList() {
        Food saved = repository.save(testFood);

        repository.delete(saved);
        Optional<Food> found = repository.findById(Long.valueOf(saved.getId()));

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindFoodsByName_ReturnsFoods() {
        Food food1 = new Food(1, "Kanan rintafilee", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 285.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food3 = new Food(3, "Kalkkunaleike", "", 100, 175.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        repository.save(food1);
        repository.save(food2);
        repository.save(food3);

        List<Food> found = repository.findFoodsByNameContainingIgnoreCase("kA");

        assertEquals(2, found.size());
        assertEquals("Kanan rintafilee", found.get(0).getName());
        assertEquals("Kalkkunaleike", found.get(1).getName());
    }

    @Test
    public void testFindFoodsByName_ReturnsEmptyList() {
        Food food1 = new Food(1, "Kanan rintafilee", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 285.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food3 = new Food(3, "Kalkkunaleike", "", 100, 175.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        repository.save(food1);
        repository.save(food2);
        repository.save(food3);

        List<Food> found = repository.findFoodsByNameContainingIgnoreCase("mai");

        assertEquals(0, found.size());
    }
}
