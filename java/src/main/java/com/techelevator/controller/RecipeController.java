package com.techelevator.controller;

import com.techelevator.dao.RecipeDao;
import com.techelevator.dao.UserDao;
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
@RequestMapping("/recipes")
@PreAuthorize("isAuthenticated()")
public class RecipeController {

    private final RecipeDao recipeDao;
    private final UserDao userDao;

    public RecipeController(RecipeDao recipeDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
    }

    @GetMapping("/my-recipes")
    @PermitAll
    public List<Recipe> getMyRecipes(Principal principal) {

        String username = principal.getName(); //getting username

        int userId = userDao.getUserByUsername(username).getId(); //get username via userdao

        return recipeDao.getRecipesByUserId(userId); // return recipes created by this user
    }
}