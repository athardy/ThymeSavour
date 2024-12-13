package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRecipeDao implements RecipeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RecipeIngredientDao recipeIngredientDao;
    private final IngredientDao ingredientDao;

    public JdbcRecipeDao(JdbcTemplate jdbcTemplate, RecipeIngredientDao recipeIngredientDao, IngredientDao ingredientDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.recipeIngredientDao = recipeIngredientDao;
        this.ingredientDao = ingredientDao;
    }

    @Override
public List<Recipe> getRecipesByUserId(int userId) {
    String sql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added " +
            "FROM recipes WHERE author = ? AND is_deleted = FALSE";
    return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
            rs.getInt("recipe_id"),
            rs.getString("recipe_name"),
            rs.getString("description"),
            rs.getString("instructions"),
            rs.getInt("author"),
            rs.getDate("date_added")
    ), userId);
}

    @Override
    public Recipe getRecipeById(int recipeId) {
        String recipeSql = "SELECT recipe_id, recipe_name, description, instructions, author, date_added " +
                "FROM recipes WHERE recipe_id = ? AND is_deleted = FALSE";

        Recipe recipe = jdbcTemplate.queryForObject(recipeSql, (rs, rowNum) -> new Recipe(
                rs.getInt("recipe_id"),
                rs.getString("recipe_name"),
                rs.getString("description"),
                rs.getString("instructions"),
                rs.getInt("author"),
                rs.getDate("date_added")
        ), recipeId);

        String ingredientsSql = "SELECT ri.id, ri.quantity, ri.unit, i.ingredient_name, ri.ingredient_id " +
                "FROM recipe_ingredient ri " +
                "JOIN ingredients i ON ri.ingredient_id = i.ingredient_id " +
                "WHERE ri.recipe_id = ?";

        List<RecipeIngredient> ingredients = jdbcTemplate.query(ingredientsSql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"), rs.getString("ingredient_name"), 0);  // Create Ingredient object
            return new RecipeIngredient(
                    rs.getInt("id"),
                    recipeId,
                    rs.getInt("ingredient_id"),
                    ingredient,
                    rs.getDouble("quantity"),  // Ensure quantity is a double
                    rs.getString("unit")
            );
        }, recipeId);

        recipe.setIngredients(ingredients);
        return recipe;
    }

    @Override
    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        String sql = "SELECT ri.id, ri.quantity, ri.unit, i.ingredient_name, ri.ingredient_id " +
                "FROM recipe_ingredient ri " +
                "JOIN ingredients i ON ri.ingredient_id = i.ingredient_id " +
                "WHERE ri.recipe_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient(rs.getInt("ingredient_id"), rs.getString("ingredient_name"), 0);  // Create Ingredient object
            return new RecipeIngredient(
                    rs.getInt("id"),
                    recipeId,
                    rs.getInt("ingredient_id"),
                    ingredient,
                    rs.getDouble("quantity"),  // Ensure quantity is a double
                    rs.getString("unit")
            );
        }, recipeId);
    }

    @Override
    public void createRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (recipe_name, description, instructions, author, date_added) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING recipe_id";
        int recipeId = jdbcTemplate.queryForObject(sql, Integer.class,
                recipe.getRecipe_name(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getAuthor(), // Ensure the author is provided
                recipe.getDate_added());
        recipe.setRecipe_id(recipeId);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        String sql = "UPDATE recipes SET recipe_name = ?, description = ?, instructions = ?, date_added = ? WHERE recipe_id = ?";
        jdbcTemplate.update(sql, recipe.getRecipe_name(), recipe.getDescription(), recipe.getInstructions(),
                recipe.getDate_added(), recipe.getRecipe_id());

        String deleteIngredientsSql = "DELETE FROM recipe_ingredient WHERE recipe_id = ?";
        jdbcTemplate.update(deleteIngredientsSql, recipe.getRecipe_id());

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            Ingredient existingIngredient = ingredientDao.getIngredientByName(ingredient.getIngredient_name());
            if (existingIngredient == null) {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setIngredient_name(ingredient.getIngredient_name());
                newIngredient.setType_id(ingredient.getType_id());
                ingredientDao.createIngredient(newIngredient);
                existingIngredient = newIngredient;
            }
            recipeIngredientDao.linkIngredientToRecipe(recipe.getRecipe_id(), existingIngredient.getIngredient_id(),
                    ingredient.getQuantity(), ingredient.getUnit());
        }
    }

    @Override
    public void softDeleteRecipe(int recipeId) {
        String sql = "UPDATE recipes SET is_deleted = TRUE WHERE recipe_id = ?";
        jdbcTemplate.update(sql, recipeId);
    }

    @Override
    public List<String> getRecipeNames(int userId) {
        String sql = "SELECT DISTINCT recipe_name FROM recipes WHERE author = ? AND is_deleted = FALSE";
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }

    public void linkIngredientToRecipe(int recipeId, int ingredientId, double quantity, String unit) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (recipe_id, ingredient_id) " +
                "DO UPDATE SET quantity = EXCLUDED.quantity, unit = EXCLUDED.unit";

        try {
            System.out.println("Executing query:");
            System.out.println(sql);
            System.out.println("Parameters: " + recipeId + ", " + ingredientId + ", " + quantity + ", " + unit);

            jdbcTemplate.update(sql, recipeId, ingredientId, quantity, unit);

            System.out.println("Ingredient linked successfully.");
        } catch (Exception e) {
            System.err.println("Error linking ingredient: " + e.getMessage());
            throw e;
        }
    }


    
}