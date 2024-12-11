package com.techelevator.dao;

import com.techelevator.model.RecipeIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientDao {

    private final JdbcTemplate jdbcTemplate;

    public RecipeIngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to link ingredients to a recipe in the recipe_ingredient table
    public void linkIngredientToRecipe(int recipeId, int ingredientId, double quantity, String unit) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, recipeId, ingredientId, quantity, unit);
    }

    // Method to delete an ingredient from a recipe
    public void deleteIngredientFromRecipe(int recipeId, int ingredientId) {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        jdbcTemplate.update(sql, recipeId, ingredientId);
    }
    
}