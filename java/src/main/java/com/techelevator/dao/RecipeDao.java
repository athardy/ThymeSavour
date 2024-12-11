package com.techelevator.dao;

import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import java.util.List;

public interface RecipeDao {

    List<Recipe> getRecipesByUserId(int userId);  // Get recipes by user

    Recipe getRecipeById(int recipeId);  // Get a specific recipe by its ID

    List<RecipeIngredient> getRecipeIngredients(int recipeId);  // Get ingredients for a specific recipe

    void createRecipe(Recipe recipe);  // Create a new recipe

    void updateRecipe(Recipe recipe);  // Update an existing recipe

    void softDeleteRecipe(int recipeId);  // Soft delete a recipe by its ID

    List<String> getRecipeNames(int userId);  // Get recipe names for a user

}