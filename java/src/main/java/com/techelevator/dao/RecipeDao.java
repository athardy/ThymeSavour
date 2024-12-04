package com.techelevator.dao;

import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;

import java.util.List;

public interface RecipeDao {

    List<Recipe> getRecipes();

    List<Recipe> getRecipesByUserId(int id);

    Recipe getRecipeByRecipeId(int id);

    void createNewRecipe(Recipe recipe);

    List<RecipeIngredient> getIngredientsForRecipe(int recipeId);

    void addIngredientToRecipe(RecipeIngredient recipeIngredient);

    void editIngredientToRecipe(int id, RecipeIngredient recipeIngredient);
}
