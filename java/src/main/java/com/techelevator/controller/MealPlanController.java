package com.techelevator.controller;

import com.techelevator.dao.JdbcGroceryListDao;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public MealPlanController(MealPlanDao mealPlanDao, UserDao userDao, RecipeDao recipeDao) {
        this.mealPlanDao = mealPlanDao;
        this.userDao = userDao;
        this.recipeDao = recipeDao;
    }



    @PostMapping
    public ResponseEntity<String> createMealPlan(@RequestBody MealPlan mealPlan, Principal principal) {
        try {
            System.out.println("Debug: Received request to create meal plan.");
            System.out.println("Name: " + mealPlan.getMeal_plan_name());
            System.out.println("Start Date: " + mealPlan.getStart_date());
            System.out.println("End Date: " + mealPlan.getEnd_date());

            // Ensure start_date and end_date are in java.sql.Date format
            Date startDate = new java.sql.Date(mealPlan.getStart_date().getTime());
            Date endDate = new java.sql.Date(mealPlan.getEnd_date().getTime());

            mealPlan.setStart_date(startDate);
            mealPlan.setEnd_date(endDate);

            // Validate input
            if (startDate.after(endDate)) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating meal plan: " + e.getMessage());
        }
    }

@GetMapping("/all")
public List<MealPlan> getAllMealPlans(Principal principal) {
    int userId = userDao.getUserByUsername(principal.getName()).getId();
    return mealPlanDao.getMealPlansByUserId(userId);
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

    @PostMapping("/{meal_plan_id}/add-meal")
    public ResponseEntity<String> addMealToPlan(
            @PathVariable int meal_plan_id,
            @RequestBody Meal meal) {
        try {
            meal.setMeal_plan_id(meal_plan_id);
            mealPlanDao.addMealToPlan(meal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meal added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding meal: " + e.getMessage());
        }


}
    @PostMapping("/meals/bulk-create")
    public ResponseEntity<String> bulkCreateMeals(@RequestBody List<Meal> meals) {
        try {
            for (Meal meal : meals) {
                mealPlanDao.addMealToPlan(meal);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Meals created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating meals: " + e.getMessage());
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
    @PostMapping("/{meal_plan_id}/generate-meals")
    public ResponseEntity<String> generateMeals(@PathVariable int meal_plan_id) {
        try {
            mealPlanDao.generateMealsForPlan(meal_plan_id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Meals generated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating meals: " + e.getMessage());
        }
    }
    @GetMapping("/{meal_plan_id}/grocery-list")
    public ResponseEntity<List<Map<String, Object>>> getGroceryListForMealPlan(@PathVariable int meal_plan_id) {
        try {
            // Fetch grocery list for the given meal plan ID
            JdbcGroceryListDao groceryListDao = null;
            List<Map<String, Object>> groceryList = groceryListDao.getGroceryListByMealPlan(meal_plan_id);
            return ResponseEntity.ok(groceryList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/{meal_plan_id}")
    public ResponseEntity<String> deleteMealPlan(@PathVariable int meal_plan_id) {
        try {
            mealPlanDao.deleteMealPlan(meal_plan_id); // Ensure `deleteMealPlan` exists in your DAO
            return ResponseEntity.ok("Meal plan deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting meal plan: " + e.getMessage());
        }
    }

}