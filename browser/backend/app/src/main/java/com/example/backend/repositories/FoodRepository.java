package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findFoodsByNameContainingIgnoreCase(String name);
}