package com.techelevator.dao;

import com.techelevator.model.Ingredient;

import java.util.List;

public interface IngredientDao {

    void createIngredient(Ingredient ingredient);

    List<Ingredient> getAllIngredients();

    List<Ingredient> getIngredientByTypeId();

    Ingredient getIngredientByName();

    Ingredient deleteIngredient();

}
