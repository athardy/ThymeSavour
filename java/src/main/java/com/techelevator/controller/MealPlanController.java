package com.techelevator.controller;

import com.techelevator.dao.MealPlanDao;
import com.techelevator.dao.UserDao;
import com.techelevator.dao.RecipeDao;
import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import com.techelevator.model.Recipe;
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
    private final RecipeDao recipeDao;

    public MealPlanController(MealPlanDao mealPlanDao, UserDao userDao, RecipeDao recipeDao) {
        this.mealPlanDao = mealPlanDao;
        this.userDao = userDao;
        this.recipeDao = recipeDao;
    }

    @PostMapping
public ResponseEntity<String> createMealPlan(@RequestBody MealPlan mealPlan, Principal principal) {
    try {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        mealPlan.setUser_id(userId);
        mealPlanDao.createMealPlan(mealPlan);
        return ResponseEntity.status(HttpStatus.CREATED).body("Meal plan created successfully!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating meal plan: " + e.getMessage());
    }
}

@GetMapping("/all")
public List<MealPlan> getAllMealPlans(Principal principal) {
    int userId = userDao.getUserByUsername(principal.getName()).getId();
    return mealPlanDao.getMealPlansByUserId(userId);
}

    @GetMapping("/{meal_plan_id}/meals")
    public List<Meal> getRecipesForMealPlan(@PathVariable int meal_plan_id) {
        return mealPlanDao.getRecipesForMealPlan(meal_plan_id);
    }

    @PutMapping("/{meal_id}/toggle-delete")
    public ResponseEntity<String> toggleMealDelete(@PathVariable int meal_id) {
        try {
            mealPlanDao.toggleMealDelete(meal_id);
            return ResponseEntity.ok("Meal toggle delete successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling meal delete: " + e.getMessage());
        }
    }

    @PostMapping("/{meal_plan_id}/share")
    public ResponseEntity<String> shareMealPlan(@PathVariable int meal_plan_id) {
        try {
            // Generate a shareable link or content
            String shareLink = "https://yourapp.com/meal-plans/" + meal_plan_id;
            return ResponseEntity.ok("Shareable link: " + shareLink);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sharing meal plan: " + e.getMessage());
        }
    }

    @PutMapping("/{meal_plan_id}")
public ResponseEntity<String> updateMealPlan(@PathVariable int meal_plan_id, @RequestBody MealPlan mealPlan) {
    try {
        // Ensure that the meal_plan_id in the path and in the body match
        mealPlan.setMeal_plan_id(meal_plan_id);

        // Call the DAO method to update the meal plan in the database
        mealPlanDao.updateMealPlan(mealPlan);

        return ResponseEntity.ok("Meal plan updated successfully!");
    } catch (Exception e) {
        // Return an internal server error if something goes wrong
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating meal plan: " + e.getMessage());
    }
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

@PostMapping("/{meal_plan_id}/meals")
public ResponseEntity<String> addMealToPlan(@PathVariable int meal_plan_id, @RequestBody Meal meal) {
    try {
        meal.setMeal_plan_id(meal_plan_id);
        mealPlanDao.addMealToPlan(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body("Meal added to meal plan.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding meal to plan: " + e.getMessage());
    }
}
}