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

    @Override
    public Food update(Long id, Food food) {
        Food existingFood = getRepository().findById(id).orElse(null);

        System.out.println("\n\n" + food + "\n\n");
        System.out.println("\n\n" + existingFood + "\n\n");

        if (existingFood != null) {
            // TODO: Implement update logic
            return getRepository().save(existingFood);
        }
        return null;
    }
}