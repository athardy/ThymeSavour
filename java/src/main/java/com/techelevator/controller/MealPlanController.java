package com.techelevator.controller;

import com.techelevator.dao.MealPlanDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/meal-plans")
@PreAuthorize("isAuthenticated()")
public class MealPlanController {
    private final MealPlanDao mealPlanDao;
    private final UserDao userDao;

    public MealPlanController(MealPlanDao mealPlanDao, UserDao userDao) {
        this.mealPlanDao = mealPlanDao;
        this.userDao = userDao;
    }

    @PostMapping("/create") //add principal to the method to pass in user id
    public ResponseEntity<String> createMealPlan(@RequestBody MealPlan mealPlan, Principal principal) {
        try {
            String username = principal.getName();
            int userId = userDao.getUserByUsername(username).getId();
            mealPlan.setUser_id(userId);

            mealPlanDao.createMealPlan(mealPlan);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meal plan created!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<MealPlan> getAllMealPlans(){
        return mealPlanDao.getAllMealPlans();
    }

    //need a method for this specific endpoint
    @GetMapping("{meal_plan_id}/recipes")
    public List<Meal> getRecipesForMealPlan(@PathVariable int meal_plan_id) {
        return null;
    }

    //added
    @DeleteMapping("/delete/{meal_plan_id}")
    public ResponseEntity<String> deleteMealPlanById(@PathVariable int meal_plan_id) {
        try {
            mealPlanDao.deleteMealPlan(meal_plan_id);
            return ResponseEntity.status(HttpStatus.OK).body("Meal plan deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }


}
