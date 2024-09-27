package com.example.backend.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entities.Food;

@RestController
@RequestMapping("/api/foods")
public class FoodController extends BaseController<Food> {}