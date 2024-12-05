package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcMealPlanDao implements MealPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMealPlanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override //add principal to the method to pass in user id
    public void createMealPlan(MealPlan mealPlan) {
    String sql = "INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES(?, ?, ?, ?)";
    jdbcTemplate.update(sql, mealPlan.getMeal_plan_name(), mealPlan.getUser_id(), mealPlan.getStart_date(), mealPlan.getEnd_date());
    }

    @Override
    public List<MealPlan> getAllMealPlans() {
        return null;
    }

    @Override
    public List<MealPlan> getMealPlansByUserId(int user_id) {
        return null;
    }

    @Override
    public MealPlan getMealPlanById(int meal_plan_id) {
        return null;
    }

    @Override
    public void updateMealPlan(MealPlan mealPlan) {

    }

    @Override
    public void addRecipeToMealPlan(Meal meal) {

    }

    @Override
    public void editRecipeToMealPlan(int meal_plan_id, Meal meal) {

    }

    @Override
    public void deleteRecipeFromMealPlan(int meal_id) {

    }

    @Override
    public void deleteMealPlan(int meal_plan_id) {

    }
}
