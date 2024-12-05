package com.techelevator.dao;

import com.techelevator.model.Ingredient;
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

    @Override
    public List<Recipe> getRecipesByUserId(int id) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes WHERE author = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while(results.next()) {
            Recipe recipe = mapRowToRecipe(results);
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeByRecipeId(int id) {
        return null;
    }

    @Override
    public void createNewRecipe(Recipe recipe){
        String sql = "INSERT INTO recipes (recipe_name, description, instructions, author) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, recipe.getRecipe_name(), recipe.getDescription(), recipe.getInstructions(), recipe.getAuthor());
    }

    @Override
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, recipe_id, ingredient_id, quantity, unit FROM recipe_ingredient WHERE recipe_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, recipeId);

        while (results.next()) {
            RecipeIngredient ingredient = mapRowToRecipeIngredient(results);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    @Override
    public void addIngredientToRecipe(RecipeIngredient recipeIngredient) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(sql, recipeIngredient.getRecipe_id(), recipeIngredient.getIngredient_id(), recipeIngredient.getQuantity(), recipeIngredient.getUnit());
    }

    @Override
    public void editIngredientToRecipe(int id, RecipeIngredient recipeIngredient) {
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



    private Recipe mapRowToRecipe(SqlRowSet results) {
        Recipe recipe = new Recipe();
        recipe.setRecipe_id(results.getInt("recipe_id"));
        recipe.setRecipe_name(results.getString("recipe_name"));
        recipe.setDescription(results.getString("description"));
        recipe.setInstructions(results.getString("instructions"));
        recipe.setAuthor(results.getInt("author"));
        recipe.setDate_added(results.getTimestamp("date_added"));
        return recipe;
    }

    private RecipeIngredient mapRowToRecipeIngredient(SqlRowSet results) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(results.getInt("id"));
        recipeIngredient.setRecipe_id(results.getInt("recipe_id"));
        recipeIngredient.setIngredient_id(results.getInt("ingredient_id"));
        recipeIngredient.setQuantity(results.getDouble("quantity"));
        recipeIngredient.setUnit(results.getString("unit"));
        return recipeIngredient;
    }

}
