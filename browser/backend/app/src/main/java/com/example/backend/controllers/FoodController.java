package com.example.backend.controllers;
import com.example.backend.entities.Log;
import com.example.backend.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entities.Food;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController extends BaseController<Food, FoodService> {
    @GetMapping("/query")
    public ResponseEntity<List<Food>> getFoodsByQuery(@RequestParam String query) {
        return service.getFoodsByQuery(query);
    }
}