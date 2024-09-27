package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {}