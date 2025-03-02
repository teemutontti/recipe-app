package com.example.backend.controllers;

import com.example.backend.services.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.Food;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    protected final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Food> create(@Valid @RequestBody Food entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping("/query")
    public ResponseEntity<List<Food>> getFoodsByQuery(@RequestParam String query) {
        return service.getFoodsByQuery(query);
    }
}