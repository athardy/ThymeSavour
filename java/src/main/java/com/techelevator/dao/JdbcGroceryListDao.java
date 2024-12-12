package com.techelevator.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JdbcGroceryListDao implements GroceryListDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGroceryListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getGroceryList(int userId) {
        String sql = 
            "SELECT " +
            "    i.ingredient_id, " +
            "    i.ingredient_name, " +
            "    ri.unit, " +
            "    SUM(ri.quantity) AS total_quantity " +
            "FROM " +
            "    recipe_ingredient ri " +
            "JOIN " +
            "    ingredients i " +
            "    ON ri.ingredient_id = i.ingredient_id " +
            "JOIN " +
            "    recipes r " +
            "    ON ri.recipe_id = r.recipe_id " +
            "WHERE " +
            "    r.author = ? " +
            "GROUP BY " +
            "    i.ingredient_id, " +
            "    i.ingredient_name, " +
            "    ri.unit " +
            "UNION " +
            "SELECT " +
            "    i.ingredient_id, " +
            "    i.ingredient_name, " +
            "    ri.unit, " +
            "    SUM(ri.quantity) AS total_quantity " +
            "FROM " +
            "    meal_plan mp " +
            "JOIN " +
            "    meals m " +
            "    ON mp.meal_plan_id = m.meal_plan_id " +
            "JOIN " +
            "    recipe_ingredient ri " +
            "    ON ri.recipe_id = m.recipe_id " +
            "JOIN " +
            "    ingredients i " +
            "    ON ri.ingredient_id = i.ingredient_id " +
            "WHERE " +
            "    mp.user_id = ? " +
            "GROUP BY " +
            "    i.ingredient_id, " +
            "    i.ingredient_name, " +
            "    ri.unit;";

        return jdbcTemplate.queryForList(sql, userId, userId);
    }
    @Override
    public List<Map<String, Object>> getGroceryListByMealPlan(int mealPlanId) {
        String sql =
                "SELECT " +
                        "    i.ingredient_id, " +
                        "    i.ingredient_name, " +
                        "    ri.unit, " +
                        "    SUM(ri.quantity) AS total_quantity " +
                        "FROM " +
                        "    meal_plan mp " +
                        "JOIN " +
                        "    meals m " +
                        "    ON mp.meal_plan_id = m.meal_plan_id " +
                        "JOIN " +
                        "    recipe_ingredient ri " +
                        "    ON ri.recipe_id = m.recipe_id " +
                        "JOIN " +
                        "    ingredients i " +
                        "    ON ri.ingredient_id = i.ingredient_id " +
                        "WHERE " +
                        "    mp.meal_plan_id = ? " +
                        "GROUP BY " +
                        "    i.ingredient_id, " +
                        "    i.ingredient_name, " +
                        "    ri.unit";

        return jdbcTemplate.queryForList(sql, mealPlanId);
    }
}