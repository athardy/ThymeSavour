package com.techelevator.controller;

import com.techelevator.dao.MealPlanDao;
import com.techelevator.model.MealPlan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/meal-plans")
@PreAuthorize("isAuthenticated()")
public class MealPlanController {
    private final MealPlanDao mealPlanDao;

    public MealPlanController(MealPlanDao mealPlanDao) {
        this.mealPlanDao = mealPlanDao;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMealPlan(@RequestBody MealPlan mealPlan) {
        try {
            mealPlanDao.createMealPlan(mealPlan);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meal plan created!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }
}
