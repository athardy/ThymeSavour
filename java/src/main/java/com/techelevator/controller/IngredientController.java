package com.techelevator.controller;

import com.techelevator.dao.IngredientDao;
import com.techelevator.model.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ingredients")
@PreAuthorize("isAuthenticated()")
public class IngredientController {

    private final IngredientDao ingredientDao;

    public IngredientController(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    @PostMapping("/create")
public ResponseEntity<String> createIngredient(@RequestBody Ingredient ingredient) {
    try {
        ingredientDao.createIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient created!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}

    @GetMapping("/all")
    public List<Ingredient> getAllIngredients() {
        return ingredientDao.getAllIngredients();
    }

    @GetMapping("/type/{type_id}")
    public List<Ingredient> getIngredientsByTypeId(@PathVariable int type_id) {
        return ingredientDao.getIngredientByTypeId(type_id);
    }

    @GetMapping("/name/{ingredient_name}")
    public List<Ingredient> getIngredientsByName(@PathVariable String ingredient_name) {
        return ingredientDao.getIngredientsByName(ingredient_name);
    }

    @DeleteMapping("/delete/{ingredient_id}")
    public ResponseEntity<String> deleteIngredientById(@PathVariable int ingredient_id) {
        try {
            ingredientDao.deleteIngredientById(ingredient_id);
            return ResponseEntity.status(HttpStatus.OK).body("Ingredient deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }

    }
