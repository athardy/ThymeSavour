package com.techelevator.dao;


import com.techelevator.model.Ingredient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcIngredientDao implements IngredientDao{
    @Override
    public boolean createIngredient() {
        boolean success = false;
        String sql = "INSERT INTO ingredients (ingredient_name, type_id) VALUES (?, ?)";
        
        return success;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return null;
    }

    @Override
    public List<Ingredient> getIngredientByTypeId() {
        return null;
    }

    @Override
    public Ingredient getIngredientByName() {
        return null;
    }

    @Override
    public Ingredient deleteIngredient() {
        return null;
    }
}
