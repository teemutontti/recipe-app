package com.example.backend.controllers.admin;

import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/foods")
public class AdminFoodController {

    private final FoodService service;

    public AdminFoodController(FoodService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Food>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Food> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
}