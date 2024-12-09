package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class JdbcMealPlanDao implements MealPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMealPlanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createMealPlan(MealPlan mealPlan) {
        String sql = "INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, mealPlan.getMeal_plan_name(), mealPlan.getUser_id(), mealPlan.getStart_date(), mealPlan.getEnd_date());
    }

    @Override
    public List<MealPlan> getAllMealPlans() {
        String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MealPlan(
                rs.getInt("meal_plan_id"),
                rs.getString("meal_plan_name"),
                rs.getInt("user_id"),
                rs.getDate("creation_date"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        ));
    }

    @Override //added
    public List<MealPlan> getMealPlansByUserId(int user_id) {
        String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MealPlan(
                rs.getInt("meal_plan_id"),
                rs.getString("meal_plan_name"),
                rs.getInt("user_id"),
                rs.getDate("creation_date"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        ), user_id);
    }

    @Override //added
    public MealPlan getMealPlanById(int meal_plan_id) {
        String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE meal_plan_id = ?";
        return jdbcTemplate.queryForObject(sql, MealPlan.class, meal_plan_id);

    }

    @Override
    public List<Meal> getRecipesForMealPlan(int meal_plan_id) {
        String sql = "SELECT meal_id, meal_plan_id, recipe_id, meal_date, meal_type FROM meals WHERE meal_plan_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Meal(
                rs.getInt("meal_id"),
                rs.getInt("meal_plan_id"),
                rs.getInt("recipe_id"),
                rs.getDate("meal_date"),
                rs.getString("meal_type")
        ), meal_plan_id);
    }

    @Override //needs ById and changed input
    public void updateMealPlan(int meal_plan_id, MealPlan mealPlan) {
        String sql = "INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES (?, ?, ?, ?) WHERE meal_plan_id = ?";
        jdbcTemplate.update(sql, mealPlan.getMeal_plan_name(), mealPlan.getUser_id(), mealPlan.getStart_date(), mealPlan.getEnd_date(), meal_plan_id);
    }

    @Override //added
    public void addRecipeToMealPlan(Meal meal) {
        String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, meal.getMeal_plan_id(), meal.getRecipe_id(), meal.getMeal_date(), meal.getMeal_type());
    }

    // JdbcMealPlanDao.java
    @Override
    public void addRandomRecipeToMealPlan(int mealPlanId, int recipeId) {
        String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, CURRENT_DATE, 'Random')";
        jdbcTemplate.update(sql, mealPlanId, recipeId);
    }

    @Override //needs ById
    public void editRecipeToMealPlan(int meal_plan_id, Meal meal) {
        String sql = "UPDATE meals SET meal_plan_id = ?, recipe_id = ?, meal_date = ?, meal_type = ? WHERE meal_id = ?";
        jdbcTemplate.update(sql, meal.getMeal_plan_id(), meal.getRecipe_id(), meal.getMeal_date(), meal.getMeal_type());
    }

    @Override //needs ById
    public void deleteRecipeFromMealPlan(int meal_id) {
        String Sql = "DELETE FROM meals WHERE meal_id = ?";
        jdbcTemplate.update(Sql, meal_id);
    }

    @Override //needs ById
    public void deleteMealPlan(int meal_plan_id) {
        String deleteRecipeSql = "DELETE FROM meals WHERE meal_plan_id = ?";
        jdbcTemplate.update(deleteRecipeSql, meal_plan_id);
        String deleteMealPlanSql = "DELETE FROM meal_plan WHERE meal_plan_id = ?";
        jdbcTemplate.update(deleteMealPlanSql, meal_plan_id);
    }
}
