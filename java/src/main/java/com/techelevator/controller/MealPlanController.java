package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/meal-plans")
@PreAuthorize("isAuthenticated()")
public class MealPlanController {

    private final MealPlanDao mealPlanDao;
    private final UserDao userDao;
    private final RecipeDao recipeDao;
    private final GroceryListDao groceryListDao;

    public MealPlanController(MealPlanDao mealPlanDao, UserDao userDao, RecipeDao recipeDao, GroceryListDao groceryListDao) {
        this.mealPlanDao = mealPlanDao;
        this.userDao = userDao;
        this.recipeDao = recipeDao;
        this.groceryListDao = groceryListDao;
    }

    @PostMapping
    public ResponseEntity<String> createMealPlan(@RequestBody MealPlan mealPlan, Principal principal) {
        try {
            System.out.println("Debug: Received request to create meal plan.");
            System.out.println("Name: " + mealPlan.getMeal_plan_name());
            System.out.println("Start Date: " + mealPlan.getStart_date());
            System.out.println("End Date: " + mealPlan.getEnd_date());

            // Validate input fields
            if (mealPlan.getStart_date() == null || mealPlan.getEnd_date() == null || mealPlan.getMeal_plan_name() == null) {
                return ResponseEntity.badRequest().body("Missing required fields: meal plan name, start date, or end date.");
            }

            // Ensure valid date range
            if (mealPlan.getStart_date().after(mealPlan.getEnd_date())) {
                return ResponseEntity.badRequest().body("Start date cannot be after the end date.");
            }

            // Fetch user ID from Principal
            int userId = userDao.getUserByUsername(principal.getName()).getId();
            mealPlan.setUser_id(userId);

            // Persist the meal plan
            mealPlanDao.createMealPlan(mealPlan);

            return ResponseEntity.status(HttpStatus.CREATED).body("Meal plan created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating meal plan: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating meal plan: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<MealPlan> getAllMealPlans(Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        return mealPlanDao.getMealPlansByUserId(userId);
    }

    @GetMapping("/{meal_plan_id}")
    public ResponseEntity<MealPlan> getMealPlanById(@PathVariable int meal_plan_id) {
        try {
            MealPlan mealPlan = mealPlanDao.getMealPlanById(meal_plan_id);
            if (mealPlan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(mealPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{meal_plan_id}")
    public ResponseEntity<String> updateMealPlan(@PathVariable int meal_plan_id, @RequestBody MealPlan mealPlan) {
        try {
            mealPlan.setMeal_plan_id(meal_plan_id);
            mealPlanDao.updateMealPlan(mealPlan);
            return ResponseEntity.ok("Meal plan updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating meal plan: " + e.getMessage());
        }
    }

    @DeleteMapping("/{meal_plan_id}")
    public ResponseEntity<String> deleteMealPlan(@PathVariable int meal_plan_id) {
        try {
            mealPlanDao.deleteMealPlan(meal_plan_id);
            return ResponseEntity.ok("Meal plan deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting meal plan: " + e.getMessage());
        }
    }

    @GetMapping("/{meal_plan_id}/meals")
    public List<Meal> getRecipesForMealPlan(@PathVariable int meal_plan_id) {
        return mealPlanDao.getRecipesForMealPlan(meal_plan_id);
    }

    @PostMapping("/{meal_plan_id}/add-meal")
    public ResponseEntity<String> addMealToPlan(@PathVariable int meal_plan_id, @RequestBody Meal meal) {
        try {
            meal.setMeal_plan_id(meal_plan_id);
            mealPlanDao.addMealToPlan(meal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meal added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding meal: " + e.getMessage());
        }
    }

    @DeleteMapping("/meals/{mealId}")
    public ResponseEntity<String> deleteMeal(@PathVariable int mealId) {
        try {
            mealPlanDao.toggleMealDelete(mealId);
            return ResponseEntity.ok("Meal deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting meal: " + e.getMessage());
        }
    }

    @PostMapping("/{meal_plan_id}/randomly-fill-meals")
    public ResponseEntity<String> randomlyFillMeals(@PathVariable int meal_plan_id) {
        try {
            mealPlanDao.fillEmptyMeals(meal_plan_id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meals filled successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filling meals: " + e.getMessage());
        }
    }

    @GetMapping("/{meal_plan_id}/meals-with-recipes")
    public ResponseEntity<List<Map<String, Object>>> getMealsWithRecipes(@PathVariable int meal_plan_id) {
        try {
            List<Map<String, Object>> mealsWithRecipes = mealPlanDao.getMealsWithRecipes(meal_plan_id);
            return ResponseEntity.ok(mealsWithRecipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{meal_plan_id}/ingredients")
    public ResponseEntity<List<Map<String, Object>>> getMealPlanIngredients(@PathVariable int meal_plan_id) {
        try {
            List<Map<String, Object>> ingredients = groceryListDao.getGroceryListByMealPlan(meal_plan_id);
            return ResponseEntity.ok(ingredients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("error", "Failed to fetch ingredients")));
        }
    }
}