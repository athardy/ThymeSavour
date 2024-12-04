package com.techelevator.dao;


import com.techelevator.model.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcIngredientDao implements IngredientDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcIngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (ingredient_name, type_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, ingredient.getIngredient_name(), ingredient.getType_id());
    }


    @Override
    public List<Ingredient> getAllIngredients() {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ));
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
