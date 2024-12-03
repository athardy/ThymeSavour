package com.techelevator.dao;

import com.techelevator.model.Recipe;
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
    public List<Recipe> getRecipes() {
        return null;
    }

    @Override
    public List<Recipe> getRecipesByUserId(int id) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe_id, recipe_name, description, author, date_added FROM recipes WHERE author = ?";
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

    private Recipe mapRowToRecipe(SqlRowSet results) {
        Recipe recipe = new Recipe();
        recipe.setRecipe_id(results.getInt("recipe_id"));
        recipe.setRecipe_name(results.getString("recipe_name"));
        recipe.setDescription(results.getString("description"));
        recipe.setAuthor(results.getInt("author"));
        recipe.setDate_added(results.getTimestamp("date_added"));
        return recipe;
    }
}
