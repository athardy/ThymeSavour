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


    @Override  //added ORDER BY date_added DESC on all get recipe methods
    public List<Recipe> getAllRecipes() {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes ORDER BY date_added DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ));
    }

    @Override //changed //added ORDER BY date_added DESC on all get recipe methods
    public List<Recipe> getRecipesByUserId(int id) {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes WHERE author = ? ORDER BY date_added DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ), id);
    }

    @Override //added and changed name //added ORDER BY date_added DESC on all get recipe methods
    public Recipe getRecipeById(int id) {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added FROM recipes WHERE recipe_id = ? ORDER BY date_added DESC";
        return jdbcTemplate.queryForObject(sql, Recipe.class, id);
    }

    @Override
    public List<Recipe> getRecipesByIngredientId(int ingredientId) {
        String sql = "SELECT r.recipe_id, r.recipe_name, r.description, r.instructions, r.author, r.date_added \n" +
                "FROM recipes r \n" +
                "JOIN recipe_ingredient ri ON r.recipe_id = ri.recipe_id \n" +
                "WHERE ri.ingredient_id = ? " +
                " ORDER BY date_added DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ), ingredientId);
    }



    @Override
    public int createNewRecipe(Recipe recipe){
        String sql = "INSERT INTO recipes (recipe_name, description, instructions, author) VALUES (?, ?, ?, ?) RETURNING recipe_id";
        return jdbcTemplate.queryForObject(sql, Integer.class,  recipe.getRecipe_name(), recipe.getDescription(), recipe.getInstructions(), recipe.getAuthor());
    }

    @Override
public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
    String sql = "SELECT ri.id, " +
                 "       ri.recipe_id, " +
                 "       ri.ingredient_id, " +
                 "       ri.quantity, " +
                 "       ri.unit, " +
                 "       i.ingredient_name " +
                 "FROM recipe_ingredient ri " +
                 "JOIN ingredients i ON ri.ingredient_id = i.ingredient_id " +
                 "WHERE ri.recipe_id = ?"; 
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        RecipeIngredient ingredient = new RecipeIngredient(
            rs.getInt("id"),
            rs.getInt("recipe_id"),
            rs.getInt("ingredient_id"),
            rs.getDouble("quantity"),
            rs.getString("unit")
        );
        ingredient.setIngredient_name(rs.getString("ingredient_name"));
        return ingredient;
    }, recipeId);
}

@Override
public void addIngredientToRecipe(RecipeIngredient recipeIngredient) {
    Integer ingredientId = recipeIngredient.getIngredient_id();
    if (ingredientId == null || ingredientId == 0) {
        // Check if ingredient exists by name
        String checkIngredientSql = "SELECT ingredient_id FROM ingredients WHERE ingredient_name = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(checkIngredientSql, recipeIngredient.getIngredient_name());
        
        if (result.next()) {
            ingredientId = result.getInt("ingredient_id");
        } else {
            // Insert the new ingredient and get the ID
            String insertIngredientSql = "INSERT INTO ingredients (ingredient_name) VALUES (?) RETURNING ingredient_id";
            ingredientId = jdbcTemplate.queryForObject(insertIngredientSql, Integer.class, recipeIngredient.getIngredient_name());
        }

        recipeIngredient.setIngredient_id(ingredientId);
    }

    // Insert into recipe_ingredient table
    String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, 
        recipeIngredient.getRecipe_id(), 
        recipeIngredient.getIngredient_id(), 
        recipeIngredient.getQuantity(), 
        recipeIngredient.getUnit()
    );
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

    @Override
    public Recipe getRandomRecipe() {
        String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added " +
                "FROM recipes ORDER BY RANDOM() LIMIT 1";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ));
    }


    // deleted mapping methods
}
