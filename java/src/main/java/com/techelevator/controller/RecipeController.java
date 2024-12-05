package com.techelevator.controller;

import com.techelevator.dao.RecipeDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Recipe;
import com.techelevator.model.RecipeIngredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/recipes")
@PreAuthorize("isAuthenticated()")
public class RecipeController {

    private final RecipeDao recipeDao;
    private final UserDao userDao;

    public RecipeController(RecipeDao recipeDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
    }

    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    @GetMapping("/my-recipes")
    @PermitAll
    public List<Recipe> getMyRecipes(Principal principal) {

        String username = principal.getName(); //getting username

        int userId = userDao.getUserByUsername(username).getId(); //get username via userdao

        return recipeDao.getRecipesByUserId(userId); // return recipes created by this user
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRecipe(@RequestBody Recipe recipe, Principal principal){
        try {
            String username = principal.getName();
            int userId = userDao.getUserByUsername(username).getId();
            recipe.setAuthor(userId);

            recipeDao.createNewRecipe(recipe);
            return  ResponseEntity.status(HttpStatus.CREATED).body("Recipe created!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }

    @GetMapping("/{recipeId}/ingredients")
    public List<RecipeIngredient> getIngredientsForRecipe(@PathVariable int recipeId) {
        return recipeDao.getIngredientsForRecipe(recipeId);
    }

    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<String> addIngredientsToRecipe(@PathVariable int recipeId, @RequestBody RecipeIngredient recipeIngredient ) {
        try {
            recipeIngredient.setRecipe_id(recipeId);
            recipeDao.addIngredientToRecipe(recipeIngredient);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient added to recipe");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
         }
    }

    @PutMapping("/{Id}/ingredients") //use the PK (id) as the path variable
    public ResponseEntity<String> editIngredientsToRecipe(@PathVariable int id, @RequestBody RecipeIngredient recipeIngredient) {
        try {
            recipeIngredient.setRecipe_id(id);
            recipeDao.editIngredientToRecipe(id, recipeIngredient);
            return ResponseEntity.status(HttpStatus.CREATED).body("Edited ingredient in recipe");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{recipe_id}")
    public ResponseEntity<String> deleteRecipeById(@PathVariable int recipe_id){
        try {
            recipeDao.deleteRecipeById(recipe_id);
            return ResponseEntity.status(HttpStatus.OK).body("Recipe deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }

}