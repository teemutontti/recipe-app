package com.example.backend.controllers;

import com.example.backend.services.FoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.Food;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    protected FoodService service;

    @PostMapping
    public ResponseEntity<Food> create(@Valid @RequestBody Food entity) {
        return service.create(entity);
    }

    @GetMapping
    public ResponseEntity<Page<Food>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        System.out.println("page: " + page + ", size: " + size);
        return service.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Food> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Food> update(@PathVariable("id") Long id, @Valid @RequestBody Food updated) {
        ResponseEntity<Food> response = service.getById(id);

        if (!response.hasBody()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Food existingFood = response.getBody();

        if (existingFood == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingFood.setName(updated.getName());
        existingFood.setCalories(updated.getCalories());
        existingFood.setBarcode(updated.getBarcode());
        existingFood.setServingSize(updated.getServingSize());
        existingFood.setCarbs(updated.getCarbs());
        existingFood.setProtein(updated.getProtein());
        existingFood.setFat(updated.getFat());
        existingFood.setEditedBy(updated.getEditedBy());
        existingFood.setEdited(updated.getEdited());
        // NOTE: Not updating createdBy and created

        return service.update(id, existingFood);
    }

    @GetMapping("/query")
    public ResponseEntity<List<Food>> getFoodsByQuery(@RequestParam String query) {
        return service.getFoodsByQuery(query);
    }
}