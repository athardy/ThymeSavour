package com.techelevator.model;

import java.util.Date;
import java.util.List;

public class Recipe {

    private int recipe_id;
    private String recipe_name;
    private String description;
    private String instructions;
    private int author;
    private Date date_added;
    private List<Ingredient> ingredients;

    public Recipe() {};

    public Recipe(int recipe_id, String recipe_name, String description, String instructions, int author, Date date_added, List<Ingredient> ingredients) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.description = description;
        this.instructions = instructions;
        this.author = author;
        this.date_added = date_added;
        this.ingredients = ingredients;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
