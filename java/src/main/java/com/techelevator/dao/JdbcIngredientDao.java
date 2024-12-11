package com.techelevator.dao;

import com.techelevator.model.Ingredient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE is_deleted = FALSE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ));
    }

    @Override
    public List<Ingredient> getIngredientByTypeId(int typeId) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE type_id = ? AND is_deleted = FALSE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ), typeId);
    }

    @Override
    public void deleteIngredientById(int ingredientId) {
        String sql = "UPDATE ingredients SET is_deleted = TRUE WHERE ingredient_id = ?";
        jdbcTemplate.update(sql, ingredientId);
    }

    @Override
    public List<Ingredient> getIngredientsByName(String ingredientName) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE ingredient_name ILIKE ? AND is_deleted = FALSE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getInt("type_id")
        ), "%" + ingredientName + "%");
    }

    @Override
    public Ingredient getIngredientByName(String ingredientName) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE ingredient_name = ? AND is_deleted = FALSE";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Ingredient(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getInt("type_id")
            ), ingredientName);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no ingredient is found
        }
    }

    // New method to get Ingredient by ID
    @Override
    public Ingredient getIngredientById(int ingredientId) {
        String sql = "SELECT ingredient_id, ingredient_name, type_id FROM ingredients WHERE ingredient_id = ? AND is_deleted = FALSE";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Ingredient(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getInt("type_id")
            ), ingredientId);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no ingredient is found
        }
    }
}