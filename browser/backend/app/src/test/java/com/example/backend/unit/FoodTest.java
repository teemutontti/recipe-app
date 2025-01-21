package com.example.backend.unit;

import com.example.backend.entities.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodTest {

    private Food testFood;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
    }

    @Test
    public void testFoodUpdate() {
        Food updatedFood = new Food(1, "Kalkkunan rintafilee", "098764321", 400, 134.0, 24.0, 8.0, 4.0, 1, 1, "", "");
        boolean result = testFood.update(updatedFood);

        assertTrue(result);
        assertEquals("Kalkkunan rintafilee", testFood.getName());
        assertEquals("098764321", testFood.getBarcode());
        assertEquals(400, testFood.getServingSize());
        assertEquals(134.0, testFood.getCalories());
        assertEquals(24.0, testFood.getCarbs());
        assertEquals(8.0, testFood.getProtein());
        assertEquals(4.0, testFood.getFat());
    }
}
