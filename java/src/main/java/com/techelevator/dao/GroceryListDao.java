package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;

import java.util.List;
import java.util.Map;

public interface GroceryListDao {

    List<Map<String, Object>> getGroceryList(int userId);
}
