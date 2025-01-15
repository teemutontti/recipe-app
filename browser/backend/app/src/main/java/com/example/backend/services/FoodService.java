package com.example.backend.services;
import com.example.backend.entities.Food;
import com.example.backend.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService extends BaseService<Food> {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    protected FoodRepository getRepository() {
        return foodRepository;
    }

    public ResponseEntity<List<Food>> getFoodsByQuery(String query) {
        System.out.println("Food being queried...");
        try {
            List<Food> data = foodRepository.findFoodsByNameContainingIgnoreCase(query);
            System.out.println(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}