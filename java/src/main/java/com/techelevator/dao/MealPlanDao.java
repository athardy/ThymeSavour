package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;

import java.util.List;

public interface MealPlanDao {

    void createMealPlan(MealPlan mealPlan);

    List<MealPlan> getAllMealPlans();

    List<MealPlan> getMealPlansByUserId(int user_id);

    MealPlan getMealPlanById(int meal_plan_id);

    void updateMealPlan(MealPlan mealPlan);

    void addRecipeToMealPlan(Meal meal);

    void editRecipeToMealPlan(int meal_plan_id, Meal meal);

    void deleteRecipeFromMealPlan(int meal_id);

    void deleteMealPlan(int meal_plan_id);

}
