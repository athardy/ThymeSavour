package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import com.techelevator.model.IngredientType;

import java.util.List;

public interface IngredientDao {

    void createIngredient(Ingredient ingredient);

    List<Ingredient> getAllIngredients();

    List<Ingredient> getIngredientByTypeId(int typeId);

    void deleteIngredientById(int ingredientId);

    List<Ingredient> getIngredientsByName(String ingredientName);

    Ingredient getIngredientByName(String ingredientName);  // Existing method

    Ingredient getIngredientById(int ingredientId);  // New method to fetch ingredient by ID

    List<IngredientType> getAllIngredientTypes();
}