package com.example.backend.unit.service;

import com.example.backend.entities.Food;
import com.example.backend.entities.User;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.FoodService;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class FoodServiceTest {

    @InjectMocks
    private FoodService service;

    @Mock
    private FoodRepository repository;

    private Food testFood;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        repository.deleteAll();
    }

    @Test
    public void testAddUser() {
        when(repository.save(testFood)).thenReturn(testFood);

        ResponseEntity<Food> response = service.create(testFood);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testUpdateUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(testFood));
        when(repository.save(testFood)).thenReturn(testFood);

        ResponseEntity<Food> response = service.update(1L, testFood);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Food updated = response.getBody();
        assertNotNull(updated);
        assertEquals(1, testFood.getId());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testDeleteUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(testFood));

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
