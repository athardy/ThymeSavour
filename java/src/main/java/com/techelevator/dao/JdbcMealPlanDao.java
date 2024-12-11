package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class JdbcMealPlanDao implements MealPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMealPlanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createMealPlan(MealPlan mealPlan) {
        String sql = "INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, mealPlan.getMeal_plan_name(), mealPlan.getUser_id(), mealPlan.getStart_date(), mealPlan.getEnd_date());
    }

    @Override
public List<MealPlan> getMealPlansByUserId(int userId) {
    String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE user_id = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> new MealPlan(
            rs.getInt("meal_plan_id"),
            rs.getString("meal_plan_name"),
            rs.getInt("user_id"),
            rs.getDate("creation_date"),
            rs.getDate("start_date"),
            rs.getDate("end_date")
    ), userId);
}

    @Override
    public List<Meal> getRecipesForMealPlan(int mealPlanId) {
        String sql = "SELECT meal_id, meal_plan_id, recipe_id, meal_date, meal_type, is_deleted FROM meals WHERE meal_plan_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Meal(
                rs.getInt("meal_id"),
                rs.getInt("meal_plan_id"),
                rs.getInt("recipe_id"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getString("meal_type"),
                rs.getBoolean("is_deleted")
        ), mealPlanId);
    }

    @Override
    public void toggleMealDelete(int mealId) {
        String sql = "UPDATE meals SET is_deleted = NOT is_deleted WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealId);
    }

    @Override
    public Meal getMealById(int mealId) {
        String sql = "SELECT meal_id, meal_plan_id, recipe_id, meal_date, meal_type, is_deleted FROM meals WHERE meal_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Meal(
                rs.getInt("meal_id"),
                rs.getInt("meal_plan_id"),
                rs.getInt("recipe_id"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getString("meal_type"),
                rs.getBoolean("is_deleted")
        ), mealId);
    }

    @Override
    public void addRandomRecipeToMealPlan(int mealPlanId, int recipeId) {
        String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, CURRENT_DATE, 'Random')";
        jdbcTemplate.update(sql, mealPlanId, recipeId);
    }

    public void updateMealPlan(MealPlan mealPlan) {
        String sql = "UPDATE meal_plan SET meal_plan_name = ?, start_date = ?, end_date = ? WHERE meal_plan_id = ?";
    
        jdbcTemplate.update(sql, 
            mealPlan.getMeal_plan_name(),
            mealPlan.getStart_date(),
            mealPlan.getEnd_date(),
            mealPlan.getMeal_plan_id()
        );
    }

    @Override
public MealPlan getMealPlanById(int mealPlanId) {
    String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE meal_plan_id = ?";
    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MealPlan(
        rs.getInt("meal_plan_id"),
        rs.getString("meal_plan_name"),
        rs.getInt("user_id"),
        rs.getDate("creation_date"),
        rs.getDate("start_date"),
        rs.getDate("end_date")
    ), mealPlanId);
}
@Override
public void addMealToPlan(Meal meal) {
    String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, meal.getMeal_plan_id(), meal.getRecipe_id(), meal.getMeal_date(), meal.getMeal_type());
}
}