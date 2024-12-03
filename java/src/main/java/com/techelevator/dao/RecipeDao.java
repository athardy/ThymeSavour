package com.techelevator.dao;

import com.techelevator.model.Recipe;

import java.util.List;

public interface RecipeDao {

    List<Recipe> getRecipes();

    List<Recipe> getRecipesByUserId(int id);

    Recipe getRecipeByRecipeId(int id);

    void createNewRecipe(Recipe recipe);
}
