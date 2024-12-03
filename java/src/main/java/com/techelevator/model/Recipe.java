package com.techelevator.model;

import java.util.Date;

public class Recipe {

    private int recipe_id;
    private String recipe_name;
    private String description;
    private int author;
    private Date date_added;

    public Recipe () {};

    public Recipe(int recipe_id, String recipe_name, String description, int author, Date date_added) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.description = description;
        this.author = author;
        this.date_added = date_added;
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
}
