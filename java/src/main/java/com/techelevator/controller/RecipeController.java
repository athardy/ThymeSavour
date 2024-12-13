package com.techelevator.controller;

import com.techelevator.dao.RecipeDao;
import com.techelevator.dao.IngredientDao;
import com.techelevator.dao.RecipeIngredientDao;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import com.techelevator.model.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techelevator.dao.UserDao;  


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final RecipeIngredientDao recipeIngredientDao;
    private final UserDao userDao;

    // Constructor to inject dependencies
    public RecipeController(RecipeDao recipeDao, IngredientDao ingredientDao, RecipeIngredientDao recipeIngredientDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.recipeIngredientDao = recipeIngredientDao;
        this.userDao = userDao;  // Inject UserDao
    }

    // Endpoint to fetch a recipe by its ID
    @GetMapping("/{recipeId}")
    public Recipe getRecipeById(@PathVariable int recipeId) {
        // Fetch the recipe from the database
        Recipe recipe = recipeDao.getRecipeById(recipeId);

        // Populate ingredients for the recipe by querying the recipe_ingredient table
        List<RecipeIngredient> recipeIngredients = recipeDao.getRecipeIngredients(recipeId);

        // Loop through each RecipeIngredient to fetch the associated Ingredient
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            // Fetch the Ingredient by ingredient_id
            Ingredient ingredient = ingredientDao.getIngredientById(recipeIngredient.getIngredient_id());
            // Set the Ingredient object inside the RecipeIngredient object
            recipeIngredient.setIngredient(ingredient);
        }

        // Set the populated ingredients list to the recipe
        recipe.setIngredients(recipeIngredients);

        // Return the complete recipe with its ingredients
        return recipe;
    }

    // Endpoint to create a new recipe
    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody Recipe recipe, Principal principal) {
        try {
            // Get the user ID from the principal
            int userId = userDao.getUserByUsername(principal.getName()).getId();
            recipe.setAuthor(userId); // Set the author ID in the recipe object
            recipeDao.createRecipe(recipe); // Save the recipe
            return ResponseEntity.ok(recipe); // Return the saved recipe object
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating recipe: " + e.getMessage());
        }
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<String> updateRecipe(@PathVariable int recipeId, @RequestBody Recipe recipe) {
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            System.out.println("Validating ingredient: " + ingredient);
            if (ingredient.getIngredient_name() == null || ingredient.getIngredient_name().isEmpty()) {
                System.out.println("Invalid ingredient name: " + ingredient);
                return ResponseEntity.badRequest().body("Ingredient name cannot be null or empty");
            }
            if (ingredient.getQuantity() <= 0 || ingredient.getUnit() == null || ingredient.getUnit().isEmpty()) {
                System.out.println("Invalid quantity or unit for ingredient: " + ingredient);
                return ResponseEntity.badRequest().body("Invalid quantity or unit for ingredient");
            }
        }
        try {
            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                if (ingredient.getIngredient_name() == null || ingredient.getIngredient_name().isEmpty()) {
                    return ResponseEntity.badRequest().body("Ingredient name cannot be null or empty");
                }
                if (ingredient.getQuantity() <= 0 || ingredient.getUnit() == null || ingredient.getUnit().isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid quantity or unit for ingredient");
                }
            }
            recipeDao.updateRecipe(recipe);
            return ResponseEntity.ok("Recipe updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating recipe: " + e.getMessage());
        }

    }

    // Endpoint to delete a recipe by its ID (soft delete)
    @DeleteMapping("/{recipeId}")
    public String deleteRecipe(@PathVariable int recipeId) {
        // Perform a soft delete on the recipe by setting is_deleted = TRUE
        recipeDao.softDeleteRecipe(recipeId);
        return "Recipe soft deleted!";
    }

    // Endpoint to fetch recipe names
    @GetMapping("/names")
    public List<String> getRecipeNames(@RequestParam int userId) {
        return recipeDao.getRecipeNames(userId);
    }

    // Endpoint to fetch ingredients associated with a specific recipe
    @GetMapping("/{recipeId}/ingredients")
    public List<RecipeIngredient> getRecipeIngredients(@PathVariable int recipeId) {
        return recipeDao.getRecipeIngredients(recipeId);
    }
    @GetMapping("/my-recipes")

public List<Recipe> getRecipesByUserId(Principal principal) {
    int userId = userDao.getUserByUsername(principal.getName()).getId();
    return recipeDao.getRecipesByUserId(userId);
}
    @PostMapping("/link-ingredient")
    public ResponseEntity<String> linkIngredientToRecipe(@RequestBody RecipeIngredient recipeIngredient) {
        try {
            System.out.println("Incoming request to link ingredient:");
            System.out.println("Recipe ID: " + recipeIngredient.getRecipe_id());
            System.out.println("Ingredient ID: " + recipeIngredient.getIngredient_id());
            System.out.println("Quantity: " + recipeIngredient.getQuantity());
            System.out.println("Unit: " + recipeIngredient.getUnit());

            // Perform the link operation
            recipeIngredientDao.linkIngredientToRecipe(
                    recipeIngredient.getRecipe_id(),
                    recipeIngredient.getIngredient_id(),
                    recipeIngredient.getQuantity(),
                    recipeIngredient.getUnit()
            );

            return ResponseEntity.ok("Ingredient linked to recipe successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error linking ingredient: " + e.getMessage());
        }
    }
    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<String> deleteIngredientFromRecipe(
            @PathVariable int recipeId,
            @PathVariable int ingredientId
    ) {
        try {
            recipeIngredientDao.deleteIngredientFromRecipe(recipeId, ingredientId);
            return ResponseEntity.ok("Ingredient removed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing ingredient: " + e.getMessage());
        }
    }
}