package com.example.backend.services;
import com.example.backend.entities.Food;
import com.example.backend.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService extends BaseService<Food> {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    protected FoodRepository getRepository() {
        return foodRepository;
    }
}