package com.techelevator.dao;

import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;

import java.util.List;

public interface RecipeDao {

    List<Recipe> getAllRecipes();

    List<Recipe> getRecipesByUserId(int id);

    Recipe getRecipeById(int id); //renamed

    int createNewRecipe(Recipe recipe);

    List<RecipeIngredient> getIngredientsForRecipe(int recipeId);

    void addIngredientToRecipe(RecipeIngredient recipeIngredient);

    void editIngredientToRecipeById(int id, RecipeIngredient recipeIngredient);

    void deleteRecipeById(int recipe_id);

}
