package com.techelevator.controller;

import com.techelevator.dao.RecipeDao;
import com.techelevator.dao.IngredientDao;
import com.techelevator.dao.RecipeIngredientDao;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import com.techelevator.model.Ingredient;
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
    public String createRecipe(@RequestBody Recipe recipe) {
        // Save the recipe to the database
        recipeDao.createRecipe(recipe);

        // Handle ingredients: update or create as necessary
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = ingredientDao.getIngredientByName(recipeIngredient.getIngredient_name());

            // If the ingredient doesn't exist, create it
            if (ingredient == null) {
                ingredient = new Ingredient();
                ingredient.setIngredient_name(recipeIngredient.getIngredient_name());
                ingredient.setType_id(recipeIngredient.getType_id());
                ingredientDao.createIngredient(ingredient);  // Insert the new ingredient
            }

            // Link the ingredient to the recipe in the recipe_ingredient table
            recipeIngredient.setIngredient_id(ingredient.getIngredient_id());  // Set the correct ingredient_id
            recipeIngredientDao.linkIngredientToRecipe(recipe.getRecipe_id(), ingredient.getIngredient_id(), recipeIngredient.getQuantity(), recipeIngredient.getUnit());
        }

        return "Recipe created successfully!";
    }

    // Endpoint to update a recipe (including its ingredients)
    @PutMapping("/{recipeId}")
    public String updateRecipe(@PathVariable int recipeId, @RequestBody Recipe recipe) {
        // Update the recipe details
        recipeDao.updateRecipe(recipe);

        // Handle ingredients: update or create as necessary
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = ingredientDao.getIngredientByName(recipeIngredient.getIngredient_name());

            // If the ingredient doesn't exist, create it
            if (ingredient == null) {
                ingredient = new Ingredient();
                ingredient.setIngredient_name(recipeIngredient.getIngredient_name());
                ingredient.setType_id(recipeIngredient.getType_id());
                ingredientDao.createIngredient(ingredient);  // Insert the new ingredient
            }

            // Link the ingredient to the recipe in the recipe_ingredient table
            recipeIngredient.setIngredient_id(ingredient.getIngredient_id());  // Set the correct ingredient_id
            recipeIngredientDao.linkIngredientToRecipe(recipeId, ingredient.getIngredient_id(), recipeIngredient.getQuantity(), recipeIngredient.getUnit());
        }

        return "Recipe updated successfully!";
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
}