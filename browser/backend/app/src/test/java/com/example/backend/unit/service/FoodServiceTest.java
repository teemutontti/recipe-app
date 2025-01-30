package com.example.backend.unit.service;

import com.example.backend.config.SecurityConfig;
import com.example.backend.entities.Food;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.FoodService;
import com.example.backend.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class FoodServiceTest {

    @InjectMocks
    private FoodService service;

    @Mock
    private FoodRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Food testFood;

    @BeforeEach
    public void setup() {
        testFood = new Food(1, "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        repository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testSaveFood_ReturnsFood() {
        when(repository.save(any(Food.class))).thenReturn(testFood);

        ResponseEntity<Food> response = service.create(testFood);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testFindById_ReturnsFood() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(testFood));

        ResponseEntity<Food> response = service.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testGetAll_ReturnsMultipleFoods() {
        // Arrange
        Food food1 = new Food(1, "Kana", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");
        Food food2 = new Food(2, "Riisi", "", 100, 250.0, 0.0, 0.0, 0.0, 1, 1, "", "");

        List<Food> foods = new ArrayList<>();
        foods.add(food1);
        foods.add(food2);

        Pageable pageable = PageRequest.of(1, 10);
        Page<Food> mockPage = new PageImpl<>(foods, pageable, foods.size());

        when(repository.findAll(any(Pageable.class))).thenReturn(mockPage);

        // Act
        ResponseEntity<Page<Food>> response = service.getAll(1, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testUpdateFood_ReturnsFood() {
        when(repository.findById(1L)).thenReturn(Optional.of(testFood));
        when(repository.save(any(Food.class))).thenReturn(testFood);

        testFood.setName("Riisi");
        testFood.setCalories(245.0);
        ResponseEntity<Food> response = service.update(1L, testFood);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0);
        assertEquals("Riisi", response.getBody().getName());
        assertEquals(245.0, response.getBody().getCalories());
    }

    @Test
    public void testDeleteFood_ReturnsNullBody() {
        doNothing().when(repository).deleteById(1L);

        ResponseEntity<Food> response = service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
