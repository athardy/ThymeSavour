package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import com.techelevator.model.MealPlan;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRecipeDao implements RecipeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRecipeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Recipe> getAllRecipes() {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ));
    }

    @Override //changed
    public List<Recipe> getRecipesByUserId(int id) {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes WHERE author = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ), id);
    }

    @Override //added and changed name
    public Recipe getRecipeById(int id) {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes WHERE recipe_id = ?";
        return jdbcTemplate.queryForObject(sql, Recipe.class, id);
    }

    @Override
    public void createNewRecipe(Recipe recipe){
        String sql = "INSERT INTO recipes (recipe_name, description, instructions, author) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, recipe.getRecipe_name(), recipe.getDescription(), recipe.getInstructions(), recipe.getAuthor());
    }

    @Override //changed needs ByRecipeId
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        String sql = "SELECT id, recipe_id, ingredient_id, quantity, unit FROM recipe_ingredient WHERE recipe_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RecipeIngredient(
                rs.getInt("id"),
                rs.getInt("recipe_id"),
                rs.getInt("ingredient_id"),
                rs.getDouble("quantity"),
                rs.getString("unit")
        ), recipeId);
    }

    @Override
    public void addIngredientToRecipe(RecipeIngredient recipeIngredient) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(sql, recipeIngredient.getRecipe_id(), recipeIngredient.getIngredient_id(), recipeIngredient.getQuantity(), recipeIngredient.getUnit());
    }

    @Override //renamed
    public void editIngredientToRecipeById(int id, RecipeIngredient recipeIngredient) {
        String sql = "UPDATE recipe_ingredient SET ingredient_id = ?, quantity = ?, unit = ? WHERE id = ?";
        jdbcTemplate.update(sql, recipeIngredient.getIngredient_id(), recipeIngredient.getQuantity(), recipeIngredient.getUnit(), id);
    }

    @Override
    public void deleteRecipeById(int recipe_id){
        String deleteIngredientsSql = "DELETE FROM recipe_ingredient WHERE recipe_id = ?";
        jdbcTemplate.update(deleteIngredientsSql, recipe_id);
        String deleteRecipeSql = "DELETE FROM recipes WHERE recipe_id = ?";
        jdbcTemplate.update(deleteRecipeSql, recipe_id);
    }
    // deleted mapping methods
}
