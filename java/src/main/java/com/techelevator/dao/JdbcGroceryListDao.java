package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JdbcGroceryListDao implements GroceryListDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcGroceryListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getGroceryList(int userId) {

        String sql = "SELECT \n" +
                "    i.ingredient_id,\n" +
                "    i.ingredient_name,\n" +
                "    ri.unit,\n" +
                "    SUM(ri.quantity) AS total_quantity\n" +
                "FROM \n" +
                "    recipe_ingredient ri\n" +
                "JOIN \n" +
                "    ingredients i ON ri.ingredient_id = i.ingredient_id\n" +
                "JOIN \n" +
                "    recipes r ON ri.recipe_id = r.recipe_id\n" +
                "WHERE\n" +
                "    r.author = ?\n" +
                "GROUP BY \n" +
                "    i.ingredient_id, i.ingredient_name, ri.unit\n" +
                "UNION\n" +
                "\n" +
                "SELECT \n" +
                "    i.ingredient_id,\n" +
                "    i.ingredient_name,\n" +
                "    ri.unit,\n" +
                "    SUM(ri.quantity) AS total_quantity\n" +
                "FROM \n" +
                "    meal_plan mp\n" +
                "JOIN \n" +
                "    meals m ON mp.meal_plan_id = m.meal_plan_id\n" +
                "JOIN \n" +
                "    recipe_ingredient ri ON ri.recipe_id = m.recipe_id\n" +
                "JOIN \n" +
                "    ingredients i ON ri.ingredient_id = i.ingredient_id\n" +
                "WHERE\n" +
                "    mp.user_id = ?\n" +
                "GROUP BY \n" +
                "    i.ingredient_id, i.ingredient_name, ri.unit;";

         return jdbcTemplate.queryForList(sql, userId, userId);

        }
}
