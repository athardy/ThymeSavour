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
    public List<Ingredient> getIngredientByTypeId(int type_id) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE type_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ), type_id);
    }

    @Override
    public List<Ingredient> getIngredientsByName(String ingredientName) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE ingredient_name ILIKE ?";
        String searchTerm = "%" + ingredientName + "%";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ), searchTerm);
    }

    @Override
    public void deleteIngredientById(int ingredient_id) {
        String sql = "DELETE FROM ingredients WHERE ingredient_id = ?";
        jdbcTemplate.update(sql, ingredient_id);

    }
}
