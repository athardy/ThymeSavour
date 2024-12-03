package com.techelevator.controller;

import com.techelevator.dao.RecipeDao;
import com.techelevator.model.Recipe;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/recipes/")
@PreAuthorize("isAuthenticated()") //this is a test
public class RecipeController {

    private RecipeDao recipeDao;

    public RecipeController(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    int id = 1;

    @PermitAll
    @GetMapping("/my-recipes")
    public List<Recipe> getMyRecipes() {

        return recipeDao.getRecipesByUserId(id);
    }

//    private getUserIdByPrincipal(Principal principal) {
//
//    }

}
