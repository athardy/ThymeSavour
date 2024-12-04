package com.techelevator.dao;

import com.techelevator.model.Ingredient;

import java.util.List;

public interface IngredientDao {

    void createIngredient(Ingredient ingredient);

    List<Ingredient> getAllIngredients();

    List<Ingredient> getIngredientByTypeId(int type_id);

    List<Ingredient> getIngredientsByName(String ingredientName);

    void deleteIngredientById(int ingredient_id);

}
