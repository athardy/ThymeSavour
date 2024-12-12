package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;

import java.util.List;
import java.util.Map;

public interface MealPlanDao {
    void createMealPlan(MealPlan mealPlan);
    List<MealPlan> getMealPlansByUserId(int userId);  
    List<Meal> getRecipesForMealPlan(int mealPlanId);
    void toggleMealDelete(int mealId);
    void addRandomRecipeToMealPlan(int mealPlanId, int recipeId);
    Meal getMealById(int mealId);
    void updateMealPlan(MealPlan mealPlan);
    public MealPlan getMealPlanById(int mealPlanId);
    void addMealToPlan(Meal meal);

    List<Map<String, Object>> getMealsWithRecipes(int mealPlanId);

    void generateMealsForPlan(int mealPlanId);
}